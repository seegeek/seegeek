# -*- coding: utf-8 -*-
import json
import math
import uuid
import thread
import simplejson
import tornado.ioloop
import tornado.web
import tornado.escape
import tornado.options
import tornado.web
import tornado.websocket
import os.path
import uuid
import logging
from tornado import web, websocket
from flask import Flask, render_template, request, jsonify

import nuve
import config


#print uuid.uuid1() 基于时间戳的uuid值 重复机会很小
class RoomMangeObject:
	RoomList = {}
	UUIDList = []
	UserList = []
	
MyRoom = RoomMangeObject()

app = Flask(__name__)

nuve_client = nuve.Nuve(config.superserviceID, config.superserviceKey, config.nuveHost, config.nuvePort)
#myRoom = ""

#def get_room():
#	rooms = nuve_client.getRooms()
#	if len(rooms) == 0:
#		pass
#	else:
#		print 'room', rooms
#		room = rooms[0]["_id"]
#	return room

#class ChatSocketHandler(tornado.websocket.WebSocketHandler):
#    waiters = set()
#    cache = []
#    cache_size = 200
# 
#    def allow_draft76(self):
#        # for iOS 5.0 Safari
#        return True
# 
#    def open(self):
#        print "new client opened"
#        ChatSocketHandler.waiters.add(self)
# 
#    def on_close(self):
#        ChatSocketHandler.waiters.remove(self)
# 
#    @classmethod
#    def update_cache(cls, chat):
#        cls.cache.append(chat)
#        if len(cls.cache) > cls.cache_size:
#            cls.cache = cls.cache[-cls.cache_size:]
# 
#    @classmethod
#    def send_updates(cls, chat):
#        logging.info("sending message to %d waiters", len(cls.waiters))
#        for waiter in cls.waiters:
#	    print type(waiter)
#            try:
#                waiter.write_message(chat)
#            except:
#                logging.error("Error sending message", exc_info=True)
# 
#    def on_message(self, message):
#        #logging.info("got message %r", message)
#		
#        ChatSocketHandler.send_updates(message)


def get_room(imei, roomname):
	rooms = nuve_client.getRooms()

	for room in rooms:
		if room['name'] == imei:
			room = room['_id']
			return room
	response = nuve_client.createRoom(imei, roomname, None)
	print 'aaaaaaaaaaaaaaaaa', response, type(response)
	room = response['_id']
	return room

def add_user_function(body): #发起一个直播的用户，将用户的所有信息上报给我
	imei = body['IMEI']
	print 'User List Count:', MyRoom.UserList, len(MyRoom.UserList)
	for i, element in enumerate(MyRoom.UserList):
		if element['IMEI'] == imei:
			MyRoom.UserList.pop(i)
			MyRoom.UserList.append(body)
			return #直接返回，若是新的用户添加到用户列表中进行管理
	MyRoom.UserList.append(body)

class MainHandler(tornado.web.RequestHandler):
	def post(self):
		data = self.request.body
		print '-----POST ---- CreateToken request data:', data, type(data)
		
		request_data_json = simplejson.loads(data)
		add_user_function(request_data_json)
		#print request_data_json, type(request_data_json)
		username = request_data_json['username']
		role = request_data_json['role']
		roomname = request_data_json['room_name']
		imei = request_data_json['IMEI']
		#uuid = request_data_json['uuid']
		#location = request_data_json['location']
		
		myRoom = get_room(imei, roomname)
		print "username = ", username, ".role = ", role, ". room ", myRoom
		
		resp = nuve_client.createToken(myRoom, username, role)
		print "resp = ", resp
		
		#response_data = '{ "token": "%s"}' %(resp)
		response_data = {}
		#response_data['uuid'] = uuid
		response_data['token'] = resp
		#self.set_header("Access-Control-Allow-Origin","http://58.53.219.69")
		#self.set_header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
		#self.set_header("Access-Control-Allow-Headers", "Content-Type");
		self.set_header('Access-Control-Allow-Origin', '*');
		self.set_header('Access-Control-Allow-Methods', 'POST, GET, OPTIONS, DELETE');
		self.set_header('Access-Control-Allow-Headers', 'origin, content-type');
		
		self.write(response_data)

class IndexHandler(web.RequestHandler):
	def get(self):
		self.render("index.html")
		
class IndexHandler_script(web.RequestHandler):
	def get(self):
		self.render("script.js")
		
class IndexHandler_erizo(web.RequestHandler):
	def get(self):
		self.render("erizo.js")

scoket_list = [] # 存放所有socket列表
send_message_list = [] #推送列表socket
class IndexHandler(web.RequestHandler):
    def get(self):
        self.render("index.html")


#心跳函数，同时更新用户数据
def beate_update_user_info(json_message):
	IMEI = json_message['IMEI']
	
	for i, element in enumerate(MyRoom.UserList):
		if element['IMEI'] == IMEI:
			#element = json_message # 找到用户 ，直接更新数据，保持最新
			MyRoom.UserList.pop(i)
			MyRoom.UserList.append(json_message)

#更新全部socket列表信息
def update_socket_list(IMEI):
	flag = 0
	for i, element in enumerate(scoket_list):
		if element['IMEI'] == IMEI:
			flag = 1
		else:
			continue
	return flag
	
class SocketHandler(websocket.WebSocketHandler):
	def check_origin(self, origin):
		return True

	def open(self):
		#UUID = uuid.uuid1()
		#socket_array = []
		#socket_array.append(self)
		#send_message = '{"uuid": "%s"}' %(str(UUID))
		#send_message = simplejson.loads(send_message)
		#
		#only_send_message = '{"uuid": "%s"}' %(str(UUID))	
		#only_send_message = simplejson.loads(only_send_message)
		#send_message['socket'] = socket_array
		#tmp_socket_list.append(send_message)
		#print 'send_message', send_message
		
		print 'new client connect'
		#if self not in scoket_list: #socket不在socket 列表中
		#	scoket_list.append(self)
		#print 'only_send_message', only_send_message
		#self.write_message(json.dumps(only_send_message))
	def on_close(self):
		print 'client close disconnect'
		#for i, element in enumerate(tmp_socket_list):
		#	print '---socket-list', tmp_socket_list
		#	socket_array = element['socket']
		#	socket = socket_array[0]
		#	if self == socket:
		#		tmp_socket_list.pop(i)
		#print '----set-up-2', len(tmp_socket_list)
		#if self in cl:
		#	cl.remove(self)
		
		for i, element in enumerate(scoket_list):
			list = element['socket']
			if self in list: # 将关闭的socket从列表中移除
				print '+++++++++++++++++++++++++++0000000++', element
				scoket_list.pop(i)
				
	def on_message(self, message):
		#logging.info("got message %r", message)
		#message = json.dumps(message)
		print 'got message', message, type(message)
		
		json_message = simplejson.loads(message)
		
		print 'transcode json', json_message, type(json_message)
		beate_update_user_info(json_message)
		
		IMEI = json_message['IMEI']
		if update_socket_list(IMEI) != 0: #列表中有的话 不用更新， 没有的话 添加到socket列表中
			print '===========++++++++++----------------------------'
			pass
		else:
			socket = []
			socket_object = {}
			socket_object['IMEI'] = IMEI
			socket.append(self)
			socket_object['socket'] = socket
			
			print '=================================================', socket_object
			
			scoket_list.append(socket_object) #{"IMEI":fdasfdsf, "socket":[socet 对象]}

def check_add_socket_list(IMEI, id):#将推送的socket 列表添加到c2 中
	for i, element in enumerate(scoket_list):
		if element['IMEI'] == IMEI:
			if element['IMEI'] == id:
				continue
			else:
				tmp_socket = []
				tmp_socket = element['socket']
				send_message_list.append(tmp_socket[0])
			
def check_longitude(longitude, latitude, id):
	location = []
	for i, element in enumerate(MyRoom.UserList):
		if element['IMEI'] == id:
			location = element['location']
			radlat1=math.radians(longitude)  
			radlat2=math.radians(location[0]) 
			a=radlat1-radlat2
			b=math.radians(latitude)-math.radians(location[1])
			
			s=2*math.asin(math.sqrt(math.pow(math.sin(a/2),2)+math.cos(radlat1)*math.cos(radlat2)*math.pow(math.sin(b/2),2)))  
			earth_radius=6378.137  
			s=s*earth_radius  
			
			if s<0:  
				s = -s
				if s <= 1:
					check_add_socket_list(element['IMEI'], id)
			else:
				if s <= 1:
					check_add_socket_list(element['IMEI'], id)

#def add_user_iemi(uuid, id):
#	for i, element in enumerate(tmp_socket_list):
#		if element['uuid'] == uuid:
#			element['IEMI'] = id # 现在没有用户ID 使用IEMI码来代替用户ID

class ApiHandler(web.RequestHandler):

    @web.asynchronous
    def get(self, *args):
		self.finish()
		id = self.get_argument("id")
		value = self.get_argument("token")
		longitude = self.get_argument("longitude")
		latitude = self.get_argument("latitude")
		check_longitude(longitude, latitude, id)
		data = '{"key" : "%s"}' %(value)
		#data = json.dumps(data)
		for c in send_message_list:
			print '----------------send_message_data--------------------------'
			c.write_message(data)
		#self.write('{"status":"ok"}')	
    @web.asynchronous
    def post(self):
        pass

#更新每个用户位置信息函数{"IMEI":"", "location":[]}
#class MainUpdateFunction(tornado.web.RequestHandler):
#	def post(self):
#		data = self.request.body
#		request_data_json = simplejson.loads(data)
#		imei = request_data_json['IEMI']
#		location = request_data_json['location']
#		
#		
#		for j, origin_element in enumerate(MyRoom.UserList):
#			if imei == origin_element['IEMI']:
#				location = element['location']
#				origin_element['location'] = location #更新老的用户的位置信息
#				self.write('{"status":"ok"}')
#		self.write('{"status":"error"}')


class Application(tornado.web.Application):
    def __init__(self):
        handlers = [
            (r"/", MainHandler),
            (r"/websocket", ChatSocketHandler),
        ]
        settings = dict(
            cookie_secret="__TODO:_GENERATE_YOUR_OWN_RANDOM_VALUE_HERE__",
            template_path=os.path.join(os.path.dirname(__file__), "templates"),
            static_path=os.path.join(os.path.dirname(__file__), "static"),
            xsrf_cookies=True,
        )
        tornado.web.Application.__init__(self, handlers, **settings)

application = tornado.web.Application([
	(r'/', IndexHandler),
	(r'/script.js', IndexHandler_script),
	(r'/erizo.js', IndexHandler_erizo),
    (r'/token/', MainHandler),
	#(r'/websocket', ChatSocketHandler),
	(r'/ws', SocketHandler),
    (r'/api', ApiHandler),
	#(r'/update/location/', MainUpdateFunction),
])

if __name__ == "__main__":
    application.listen(5001)
    tornado.ioloop.IOLoop.instance().start()
