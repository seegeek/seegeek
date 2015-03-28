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
	print 'room_len', len(MyRoom.UserList)
	for i, element in enumerate(MyRoom.UserList):
		print "------------------------"
		print imei
		print element['IMEI']
		print "------------------------"
		if element['IMEI'] == imei:
			MyRoom.UserList.pop(i)
			MyRoom.UserList.append(body)
			return #直接返回，若是新的用户添加到用户列表中进行管理
	print 'first user add', body
	MyRoom.UserList.append(body)

class MainHandler(tornado.web.RequestHandler):
	def post(self):
		data = self.request.body
		print '-----POST ----', data,type(data)
		
		request_data_json = simplejson.loads(data)
		add_user_function(request_data_json)
		#print request_data_json, type(request_data_json)
		username = request_data_json['username']
		role = request_data_json['role']
		roomname = request_data_json['room_name']
		imei = request_data_json['IMEI']
		uuid = request_data_json['uuid']
		#location = request.get_json()['location']
		
		
		myRoom = get_room(imei, roomname)
		print "username = ", username, ".role = ", role, ". room ", myRoom
		
		resp = nuve_client.createToken(myRoom, username, role)
		print "resp = ", resp
		
		#response_data = '{ "token": "%s"}' %(resp)
		response_data = {}
		response_data['uuid'] = uuid
		response_data['token'] = resp
		#self.set_header("Access-Control-Allow-Origin","http://58.53.219.69")
		#self.set_header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
		#self.set_header("Access-Control-Allow-Headers", "Content-Type");
		self.set_header('Access-Control-Allow-Origin', '*');
    		self.set_header('Access-Control-Allow-Methods', 'POST, GET, OPTIONS, DELETE');
    		self.set_header('Access-Control-Allow-Headers', 'origin, content-type');

		
		print 'token -- uuid', response_data	
		self.write(response_data)
		#self.write("hello")

class MainHandlerHtml(tornado.web.RequestHandler):
    def get(self):
        self.render("index.html", messages=ChatSocketHandler.cache)

cl = []
tmp_socket_list = [] #UUID 和 socket的句柄
class IndexHandler(web.RequestHandler):
    def get(self):
        self.render("index.html")

class SocketHandler(websocket.WebSocketHandler):
    def check_origin(self, origin):
        return True

    def open(self):
		UUID = uuid.uuid1()
		socket_array = []
		socket_array.append(self)
		send_message = '{"uuid": "%s"}' %(str(UUID))
		send_message = simplejson.loads(send_message)
		
		only_send_message = '{"uuid": "%s"}' %(str(UUID))	
		only_send_message = simplejson.loads(only_send_message)
		send_message['socket'] = socket_array
		tmp_socket_list.append(send_message)
		print 'send_message', send_message
		
		if self not in cl:
			cl.append(self)
		print 'only_send_message', only_send_message
		self.write_message(json.dumps(only_send_message))
    def on_close(self):
		print 'closed client'
		for i, element in enumerate(tmp_socket_list):
			print '---socket-list', tmp_socket_list
			socket_array = element['socket']
			socket = socket_array[0]
			if self == socket:
				tmp_socket_list.pop(i)
		print '----set-up-2', len(tmp_socket_list)
		if self in cl:
			cl.remove(self)

def check_add_socket_list(uuid, c2):#将推送的socket 列表添加到c2 中
	for i, element in enumerate(tmp_socket_list):
		if element['uuid'] == uuid:
			print '1111111111112222222222222222222'
			socket = element['socket']
			c2.append(socket[0]) #将要推送的列表， 每个用户要推送时都要重新生成
			
def check_longitude(longitude, latitude, id, c2):
	location = []
	for i, element in enumerate(MyRoom.UserList):
		if element['IMEI'] == id:
			print '==============000000', element	
			location = element['location']
			#radlat1=rad(longitude)  
			#radlat2=rad(location[0]) 
			#a=radlat1-radlat2
			#b=rad(latitude)-rad(location[1])
			
			#s=2*math.asin(math.sqrt(math.pow(math.sin(a/2),2)+math.cos(radlat1)*math.cos(radlat2)*math.pow(math.sin(b/2),2)))  
			#earth_radius=6378.137  
			#s=s*earth_radius  
			s = 1
			if s<0:  
				s = -s
				if s <= 1:
					check_add_socket_list(element['uuid'], c2)
			else:
				if s <= 1:
					check_add_socket_list(element['uuid'], c2)

def add_user_iemi(uuid, id):
	for i, element in enumerate(tmp_socket_list):
		if element['uuid'] == uuid:
			element['IEMI'] = id # 现在没有用户ID 使用IEMI码来代替用户ID

class ApiHandler(web.RequestHandler):

    @web.asynchronous
    def get(self, *args):
		c2 = []
		self.finish()
		id = self.get_argument("id")
		uuid = self.get_argument("uuid")
		value = self.get_argument("token")
		longitude = self.get_argument("longitude")
		latitude = self.get_argument("latitude")
		add_user_iemi(uuid, id)
		check_longitude(longitude, latitude, id, c2)
		data = '{"key" : "%s"}' %(value)
		#data = json.dumps(data)
		for c in c2:
			print '----------------send_message_data--------------------------'
			c.write_message(data)
		#self.write('{"status":"ok"}')	
    @web.asynchronous
    def post(self):
        pass

#更新每个用户位置信息函数{"IMEI":"", "location":[]}
class MainUpdateFunction(tornado.web.RequestHandler):
	def post(self):
		data = self.request.body
		request_data_json = simplejson.loads(data)
		imei = request_data_json['IEMI']
		location = request_data_json['location']
		
		
		for j, origin_element in enumerate(MyRoom.UserList):
			if imei == origin_element['IEMI']:
				location = element['location']
				origin_element['location'] = location #更新老的用户的位置信息
				self.write('{"status":"ok"}')
		self.write('{"status":"error"}')

application = tornado.web.Application([
	(r"/index.html", MainHandlerHtml),
    (r"/token/", MainHandler),
	#(r"/websocket", ChatSocketHandler),
	(r'/ws', SocketHandler),
    (r'/api', ApiHandler),
	(r'/update/location/', MainUpdateFunction),
])

if __name__ == "__main__":
    application.listen(5000)
    tornado.ioloop.IOLoop.instance().start()
