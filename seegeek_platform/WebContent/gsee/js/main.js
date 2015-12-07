'use strict';
var baseUrl = "../";
var socketUrl = "ws://58.53.219.69:5000/ws";
//showUrl需要确定是否正确
var showUrl = "http://58.53.219.69/client/show.html?room_id="
var socket;
var isAlive = false;

function indexLoad() {
	console.log("onload");
	connectSeeGeekServer();
	postData({
		UserId: 15010215479,
		PasswordEn: 'salkflasjdkf',
		IMEI :'1sdfasdlfasdlkfjlsk'
	}, baseUrl + "rest/verify");
}
/*
function onHClick() {
	if (context.localStream) {
		if (context.room)
			context.room.disconnect();
		context.localStream.stop();
	}
}

function onGClick() {
	if (isAlive) {
		if (context.localStream) {
			if (context.room)
				context.room.unsubscribe(context.localStream);
			context.localStream.stop();
		}
		
		if (typeof MediaStreamTrack === 'undefined') {
			alertToast('This browser does not support MediaStreamTrack.\n\nTry Chrome.');
		} else {
			MediaStreamTrack.getSources(function (sourceInfos) {
				cameraArray = [];
				console.log("sourceInfos:%o", sourceInfos);
				for (var i = 0; i != sourceInfos.length; ++i) {
					var sourceInfo = sourceInfos[i];
					if (sourceInfo.kind === 'video') {
						alertToast("get camera " + sourceInfo.id);
						cameraArray.push(sourceInfo.id);
					}
				}
			});
		}
		requestToken();
	} else {
		alertToast("isAlive " + isAlive);
	}
}

function onSClick() {
	if (context.localStream) {
		if (context.room)
			context.room.disconnect();
		context.localStream.stop();
	}
}
*/
//连接服务器
function connectSeeGeekServer() {
	//alertToast("connectServer");
	if (window.WebSocket) {
		socket = new WebSocket(socketUrl);
		socket.onmessage = function(event){
			console.log("=====onmessage=====: ", event.data);
			var e = jQuery.parseJSON(event.data);
			console.log(e.room_id, "|||||||||", e.key);
			if (e.key) {
				showNotification("有新的直播流", showUrl + e.room_id);
			}
		}
		socket.onopen = function(event){
			console.log("=====onopen=====");
			isAlive = true;
			socket.send(JSON.stringify({
				IMEI: getIMEI(),
				location: [getLongitude(), getLatitude()]
			}));
			//self.setInterval("heartBeat()", 5000);
		}
		socket.onclose = function(event){
			console.log("=====onclose=====");
			isAlive = false;
		}
	} else {
		alertToast("=====Not support websocket=====")
	}
}

function heartBeat() {
	//alertToast("heartBeat");
	if (isAlive) {
		console.log("=====heart beat ...=====")
		socket.send(JSON.stringify({
			IMEI: getIMEI(),
			location: [getLongitude(), getLatitude()]
		}));
	} else {
		console.log("=====heart stop=====");
	}
}