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
import urllib
import time
import threading
import urllib2
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

def call_thread():
      while True:
	time.sleep(5)
	print "call_thread.................."
	roomtest =nuve_client.getRoom('559b88ae71b90ade6a6ee00f')
	print "roomtest",roomtest
	rooms = nuve_client.getRooms()
	print rooms

def get_user(roomname):
        users = nuve_client.getUsers(roomname,None)
	print users


def get_room(imei, roomname):
	rooms = nuve_client.getRooms()
	for room in rooms:
		print 'pander', room['name']
		if room['name'] == imei:
			room = room['_id']
			return room
	response = nuve_client.createRoom(imei, roomname, None)
	print 'aaaaaaaaaaaaaaaaa', response, type(response)
	room = response['_id']
	return room

def add_user_function(body, id): #发起一个直播的用户，将用户的所有信息上报给我
	imei = body['IMEI']
	print 'User List Count:', MyRoom.UserList, len(MyRoom.UserList)
	for i, element in enumerate(MyRoom.UserList):
		if element['IMEI'] == imei:
			print "add_user_function  ", body
			location=[body['Longitude'],body['Latitude']]
			print location
			element['room_id'] = id
			element['location'] = location
			element['role'] = body['Role']
			element['username'] = body['username']
			element['room_name'] = body['room_name']
			#MyRoom.UserList.pop(i)
			#MyRoom.UserList.append(body)
			return #直接返回，若是新的用户添加到用户列表中进行管理
	body['room_id'] = id
	MyRoom.UserList.append(body)

def get_room_id(room_id):
	rooms = nuve_client.getRooms()

	for room in rooms:
		if room['_id'] == room_id:
			return room['_id']
	#response = nuve_client.createRoom(imei, roomname, None)
	#print 'aaaaaaaaaaaaaaaaa', response, type(response)
	#room = response['_id']
	return room_id
########publish method 
class MainHandlerttoken(tornado.web.RequestHandler):
	def post(self):
		data = self.request.body
		request_data_json = simplejson.loads(data)
		room_id = request_data_json['room_id']
		username = request_data_json['username']
		role = request_data_json['Role']
	#	ItemId = request_data_json['ItemId']
	#	nickname = request_data_json['nickname']
	#	Location = request_data_json['Location']
		myRoom = get_room_id(room_id)
		############update role to presenter
		update_user_info(request_data_json)
		#uuid = request_data_json['uuid']
		#location = request_data_json['location']
		
		#myRoom = get_room(imei, roomname)
	#	print "username = ", username, ".role = ", role, ". room ", myRoom
		
		resp = nuve_client.createToken(myRoom, username, role)
	#	print "resp = ", resp
		
		#response_data = '{ "token": "%s"}' %(resp)
		response_data = {}
		#response_data['uuid'] = uuid
		response_data['token'] = resp
		response_data['room_id'] = myRoom
		print "response..................",response_data
		#self.set_header("Access-Control-Allow-Origin","http://58.53.219.69")
		#self.set_header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
		#self.set_header("Access-Control-Allow-Headers", "Content-Type");
		self.set_header('Access-Control-Allow-Origin', '*');
		self.set_header('Access-Control-Allow-Methods', 'POST, GET, OPTIONS, DELETE');
		self.set_header('Access-Control-Allow-Headers', 'origin, content-type');
		
		self.write(response_data)
######get token
class MainHandler(tornado.web.RequestHandler):
	def post(self):
		data = self.request.body
	#	print '-----POST ---- CreateToken request data:', data, type(data)
		request_data_json = simplejson.loads(data)
		print "request_data_json",request_data_json
		#add_user_function(request_data_json)
		#print request_data_json, type(request_data_json)
		username = request_data_json['username']
		role = request_data_json['Role']
		roomname = request_data_json['room_name']
		imei = request_data_json['IMEI']
		myRoom = get_room(imei, roomname)
		add_user_function(request_data_json, myRoom)
		#uuid = request_data_json['uuid']
		
		#myRoom = get_room(imei, roomname)
	#	print "username = ", username, ".role = ", role, ". room ", myRoom
		
		resp = nuve_client.createToken(myRoom, username, role)
		print "resp = ", resp
		
		#response_data = '{ "token": "%s"}' %(resp)
		response_data = {}
		#response_data['uuid'] = uuid
		response_data['token'] = resp
		response_data['room_id'] = myRoom
		#self.set_header("Access-Control-Allow-Origin","http://58.53.219.69")
		#self.set_header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
		#self.set_header("Access-Control-Allow-Headers", "Content-Type");
		self.set_header('Access-Control-Allow-Origin', '*');
		self.set_header('Access-Control-Allow-Methods', 'POST, GET, OPTIONS, DELETE');
		self.set_header('Access-Control-Allow-Headers', 'origin, content-type');
		print "***********************", response_data
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

socket_list = [] # 存放所有socket列表
#send_message_list = [] #推送列表socket
class IndexHandler(web.RequestHandler):
    def get(self):
        self.render("index.html")


####update the user infor
def update_user_info(json_message):
	 IMEI = json_message['IMEI']
	 for i, element in enumerate(MyRoom.UserList):
		  if element['IMEI'] == IMEI:
			element['Role'] = json_message['Role']
#心跳函数，同时更新用户数据
def beate_update_user_info(json_message):
	print "beate_update_user_info...............",json_message
	IMEI = json_message['IMEI']
	flag = 0
	for i, element in enumerate(MyRoom.UserList):
		if element['IMEI'] == IMEI:
			flag = 1
			#element = json_message # 找到用户 ，直接更新数据，保持最新
			#MyRoom.UserList.pop(i)
			#MyRoom.UserList.append(json_message)
			element['location'] = json_message['location']
			print "( element['location'])", element['location']
			
	if flag == 0:
		MyRoom.UserList.append(json_message)
#更新全部socket列表信息
def update_socket_list(IMEI):
	flag = 0
	for i, element in enumerate(socket_list):
		if element['IMEI'] == IMEI:
			flag = 1
		else:
			continue
	return flag
	
class SocketHandler(websocket.WebSocketHandler):
	def check_origin(self, origin):
		print "origin..........."
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
		if self not in socket_list: #socket不在socket 列表中
			socket_list.append(self)
		#print 'only_send_message', only_send_message
		#self.write_message(json.dumps(only_send_message))
	
	def ping(self, data):
       	    print "ping method...........",self.ws_connection
            if self.ws_connection is None:
                print "error network ........"
            self.ws_connection.write_ping(data)

        def on_pong(self, data):
              print "on_pong............",data


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
		
		for i, element in enumerate(MyRoom.UserList):
			#notify platform
			print "======================================elment"
			print element
			list = []
			list = element['socket']
			if self in list: # 将关闭的socket从列表中移除
				print 'websocket 关闭，将socket句柄移除', element
				print self
				element['socket'] = []
				if element.has_key('Role') ==True:
					role_value = element['Role']
		       	        	if   role_value == 'presenter' and not element.has_key('room_id'):
				             print "presenter...............notify java......."
				       	     #notify java platform interface api
				             room_id = element['room_id']
				             notifyJava(room_id)

	
	def on_message(self, message):
		#logging.info("got message %r", message)
		#message = json.dumps(message)
		json_message = simplejson.loads(message)
		print 'transcode json', json_message, type(json_message)
		beate_update_user_info(json_message)
		IMEI = json_message['IMEI']
		#if update_socket_list(IMEI) != 0: #列表中有的话 不用更新， 没有的话 添加到socket列表中
		#	print '列表中存在该IMEI，不需要更新'
		#	pass
		#else:
		#	socket = []
		#	socket_object = {}
		#	socket_object['IMEI'] = IMEI
		#	socket.append(self)
		#	socket_object['socket'] = socket
		#	
		#	print '列表中没有改IMEI，需要增加socket句柄和IMEI码', socket_object
		#	
		#	socket_list.append(socket_object) #{"IMEI":fdasfdsf, "socket":[socet 对象]}
		
		for i, element in enumerate(MyRoom.UserList):
			if element['IMEI'] == IMEI:
				print "------   on_message -----------------------",json_message['location']
				element['location'] = json_message['location']
				socket = [] #每个用户自己的socket句柄
				socket.append(self)
				element['socket'] = socket

def notifyJava(room_id): 
      print "notify java........................."
      test_data = {'RoomId':room_id}
      test_data_urlencode = urllib.urlencode(test_data)
      requrl = "http://58.53.219.69:8081/seegeek/rest/item/deleteItem"
      req = urllib2.Request(url = requrl,data =test_data_urlencode)
      res_data = urllib2.urlopen(req)
      res = res_data.read()
      print res
def check_add_socket_list(IMEI, id):#将推送的socket 列表添加到c2 中
	send_message_list = [] 
        print "======================"
	for i, element in enumerate(MyRoom.UserList):
		#if element['IMEI'] == IMEI:
		if element['IMEI'] == id:
			print '本机ID 不做推送'
			continue
		else:
			tmp_socket = []
			tmp_socket = element['socket']
			if len(tmp_socket):
				print '将websocket句柄加入到推送列表中：', tmp_socket
				send_message_list.append(tmp_socket[0])
	
	for i, element in enumerate(MyRoom.UserList):
		if element['IMEI'] == id:
			element['send_socket'] = send_message_list
			print '生成发送socket列表', send_message_list
			
def check_longitude(longitude, latitude, id):
	location = []
	for i, element in enumerate(MyRoom.UserList):
		if element['IMEI'] == id:
			location = element['location']
			print element
			print "++++++++++++++++++++++++ check_longitude",location
			radlat1=math.radians(longitude)  
			float_longtitude = float(location[0])
			radlat2=math.radians(float_longtitude) 
			a=radlat1-radlat2
			float_latitude = float(location[1])
			b=math.radians(latitude)-math.radians(float_latitude)
			
			s=2*math.asin(math.sqrt(math.pow(math.sin(a/2),2)+math.cos(radlat1)*math.cos(radlat2)*math.pow(math.sin(b/2),2)))  
			earth_radius=6378.137  
			s=s*earth_radius  
			
			print '经度和纬度的距离', s
			if s<0:  
				s = -s
				if s <= 1000:
					check_add_socket_list(element['IMEI'], id)
			else:
				if s <= 1000:
					check_add_socket_list(element['IMEI'], id)

#def add_user_iemi(uuid, id):
#	for i, element in enumerate(tmp_socket_list):
#		if element['uuid'] == uuid:
#			element['IEMI'] = id # 现在没有用户ID 使用IEMI码来代替用户ID

class ApiHandler(web.RequestHandler):

    @web.asynchronous
    def get(self, *args):
		self.finish()
		print "self++++++++++++",self
		id = self.get_argument("id")
		value = self.get_argument("token")
		longitude = float(self.get_argument("longitude"))
		role = self.get_argument("Role")
		latitude = float(self.get_argument("latitude"))
		ItemId = self.get_argument("ItemId")
		title = self.get_argument("title")
		nickname = self.get_argument("nickname")
		Location = self.get_argument("Location")
		print urllib.unquote(Location)
		print urllib.unquote(nickname)
		check_longitude(longitude, latitude, id)
		data = '{"key" : "%s"}' %(value)
                json_data = simplejson.loads(data)
		#####
		for i, element in enumerate(MyRoom.UserList):
			if element['IMEI'] == id:
				print element
				for socket in element['send_socket']:
					print '推送列表中的元素', element['send_socket']
					json_data['nickname'] = urllib.unquote(nickname)
					json_data['Location'] = urllib.unquote(Location)
					json_data['ItemId'] = str(ItemId)
					json_data['title'] = urllib.unquote(title)
					#get_user(element['room_id'])
					print "print send messs.................."
					socket.write_message(json_data)
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


#class Application(tornado.web.Application):
#    def __init__(self):
#        handlers = [
#            (r"/", MainHandler),
#            (r"/websocket", ChatSocketHandler),
#        ]
#        settings = dict(
#            cookie_secret="__TODO:_GENERATE_YOUR_OWN_RANDOM_VALUE_HERE__",
#            template_path=os.path.join(os.path.dirname(__file__), "templates"),
#            static_path=os.path.join(os.path.dirname(__file__), "static"),
#            xsrf_cookies=True,
#        )
#        tornado.web.Application.__init__(self, handlers, **settings)

application = tornado.web.Application([
	(r'/', IndexHandler),
	(r'/script.js', IndexHandler_script),
	(r'/erizo.js', IndexHandler_erizo),
    (r'/token/', MainHandler),
	(r'/twicetoken/', MainHandlerttoken),
	#(r'/websocket', ChatSocketHandler),
	(r'/ws', SocketHandler),
    (r'/api', ApiHandler),
	#(r'/update/location/', MainUpdateFunction),
])

if __name__ == "__main__":
    # invoke call_thread method
    #t = threading.Thread(target=call_thread,args=())
    #t.setDaemon(True)
    #t.start()
    application.listen(5000)
    tornado.ioloop.IOLoop.instance().start()
