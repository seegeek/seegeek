var serverUrl = "/";
var localStream, room, recording, recordingId;

//function startRecording () {
//  if (room !== undefined){
//    if (!recording){
//      room.startRecording(localStream, function(id) {
//        recording = true;
//        recordingId = id;
//      });
//      
//    } else {
//      room.stopRecording(recordingId);
//      recording = false;
//    }
//  }
//}

window.onload = function () {
  recording = false;
  //var screen = getParameterByName("screen");
  var config = {audio: true, video: true, data: true, screen: false, videoSize: [640, 480, 640, 480]};
  // If we want screen sharing we have to put our Chrome extension id. The default one only works in our Lynckia test servers.
  // If we are not using chrome, the creation of the stream will fail regardless.
  if (screen){
    config.extensionId = "okeephmleflklcdebijnponpabbmmgeo";
  }
  localStream = Erizo.Stream(config);
  var createToken = function(userName, role, location, imei, room_name, callback) {
	console.log('setup-1', userName)
    var req = new XMLHttpRequest();
    //var url = serverUrl + 'createToken/';
    var url = serverUrl + 'token/';
    var body = {username: userName, role: role, location: location, IMEI: imei, room_name: room_name}; 
    req.onreadystatechange = function () {
      if (req.readyState === 4) {
        callback(req.responseText);
      }
    };

    req.open('POST', url, true);
    req.setRequestHeader('Content-Type', 'application/json');
    req.send(JSON.stringify(body));
  };

  createToken("user", "presenter", "[1123, 23455]", "aaaaaaaa", "pander", function (response) {
    console.log('setup-2---------', response);
    json_response =  eval("(" + response + ")");
    var token = json_response.token;
    room = Erizo.Room({token: token});

	if (window.WebSocket) {
		socket = new WebSocket("ws://58.53.219.69:5001/ws");
		socket.onmessage = function(event){
			
		}
		socket.onopen = function(event){
			console.log("onopen");
			send_data = '{"userName":"user", "role":"presenter", "location":["1123", "23456"], "IMEI":"aaaaaaaa", "room_name": "pander"}'
			socket.send(send_data)
		}
		socket.onclose = function(event){
			console.log("onclose");
		}
	} 
	else 
	{
		console.log("onopen------");
		alert("Not support websocket")
	}

    localStream.addEventListener("access-accepted", function () {
      var subscribeToStreams = function (streams) {
	 console.log("========================================______", streams)
        for (var index in streams) {
          var stream = streams[index];
          if (localStream.getID() !== stream.getID()) {
	    console.log('Created 5*****************************************', localStream.getID(), stream.getID());
            room.subscribe(stream);
          }
        }
      };

      room.addEventListener("room-connected", function (roomEvent) {
	console.log('Created 3 *********************************************************************************************', localStream);
        room.publish(localStream, {maxVideoBW: 300});
        subscribeToStreams(roomEvent.streams);
      });

      room.addEventListener("stream-subscribed", function(streamEvent) {
	console.log('Created 2 *********************************************************************************************');
        var stream = streamEvent.stream;
        var div = document.createElement('div');
        div.setAttribute("style", "width: 320px; height: 240px;");
        div.setAttribute("id", "test" + stream.getID());

        document.body.appendChild(div);
        stream.show("test" + stream.getID());

      });

      room.addEventListener("stream-added", function (streamEvent) {
	console.log('Created 4 *********************************************************************************************');
        var streams = [];
        streams.push(streamEvent.stream);
        subscribeToStreams(streams);
	//subscribeToStreams(stream)
        document.getElementById("recordButton").disabled = false;
      });

      room.addEventListener("stream-removed", function (streamEvent) {
        // Remove stream from DOM
        var stream = streamEvent.stream;
        if (stream.elementID !== undefined) {
          var element = document.getElementById(stream.elementID);
          document.body.removeChild(element);
        }
      });
      
      room.addEventListener("stream-failed", function (streamEvent){
          console.log("STREAM FAILED, DISCONNECTION");
          room.disconnect();

      });

      room.connect();
	  localStream.hide();
      //localStream.show("myVideo");

    });
    localStream.init();
  });
};
