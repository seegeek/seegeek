var room;
function showMedia(itemId) {
	var room_id = getRoomId(itemId);
	requestTokenByRoomId(room_id);
}

function onBackClick() {
	if (room)
		room.disconnect();
}

// 初始化licode相关内容
function requestTokenByRoomId(k) {
	//alertToast("requestToken");
	var ddd = {
		role: "presenter",
		username: "guosc",
		IMEI: getIMEI(),
		room_name: "guosc",
		room_id:k
	};
	$.ajax({
		url : "http://58.53.219.69:5000/twicetoken/",
		type : "POST",
		crossDomain: true,
		async: false,
		data: JSON.stringify(ddd),
		success: function(data) {
			console.log("==========11111", data);
			//alertToast("got token = " + data.token);
			token = data.token;
			room = Erizo.Room({token: token});
			var subscribeToStreams = function (streams) {
				console.log("==========subscribeToStreams", streams);
				for (var index in streams) {
					var stream = streams[index];
					room.subscribe(stream);
				}
			};
			
			room.addEventListener("room-connected", function (roomEvent) {
				console.log("==========room-connected", roomEvent);
				subscribeToStreams(roomEvent.streams);
			});

			room.addEventListener("stream-subscribed", function(streamEvent) {
				console.log("==========stream-subscribed", streamEvent);
				//room.subscribe(streamEvent.stream);
				streamEvent.stream.play("myVideo",{video: true});
			}); 

			room.addEventListener("stream-added", function (streamEvent) {
				var streams = [];
				console.log("==========stream-added", streamEvent);
				streams.push(streamEvent.stream);
				subscribeToStreams(streams);
			});

			room.addEventListener("stream-removed", function (streamEvent) {
				console.log("==========stream-removed", streamEvent);
				var stream = streamEvent.stream;
				if (stream.elementID !== undefined) {
					var element = document.getElementById(stream.elementID);
					//document.body.removeChild(element);
				}
			});
			console.log("==========33333");
			room.connect();
			//s.play("myVideo");
		},
		error: function(e) {
			alertToast("requestToken error: " + e);
		},
		//contentType: "application/json",
		dataType : "json"
	});
}
