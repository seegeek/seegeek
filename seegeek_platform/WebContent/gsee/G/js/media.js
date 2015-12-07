var sortTag = 1;
var offsetGet = 0;
var countGet = 3;
var offsetSearch = 0;
var countSearch = 5;
var getList, searchList;
var theItemId = "1";

var cameraArray = [];
var camera = 0;
var context = {
	localStream: null,
	room: null,
	token: null
};

function getItemList() {
	getList = getData({
		Sort: sortTag,
		Offset: offsetGet,
		Count: countGet
	}, baseUrl + "rest/item/getItemList");
	console.log(getList);
}

function getSearchList() {
	searchList = getData({
		Keyword: document.getElementById("keyword").value,
		Offset: offsetSearch,
		Count: countSearch
	}, baseUrl + "rest/item/getSearchList");
	console.log(searchList);
}

function getRoomId() {
	getData({
		ItemId: theItemId
	}, baseUrl + "rest/item/getRoomId");
	console.log(postRet);
}

function getItem() {
	getData({
		ItemId: theItemId
	}, baseUrl + "rest/item/getItem");
	console.log(getRet);
}

function getComment() {
	getData({
		ItemId: theItemId,
		Offset: 1,
		Num :1
	}, baseUrl + "rest/item/getComment");
	console.log(getRet);
}


function collect() {
	postData({
		UserId: userId,
		IsCare: true
	}, baseUrl + "rest/item/collect");
	console.log(postRet);
}
function parise() {
	postData({
		UserId: userId,
		IsCare: true
	}, baseUrl + "rest/item/parise");
	console.log(postRet);
}
function report() {
	postData({
		UserId: userId,
		IsCare: true
	}, baseUrl + "rest/item/report");
	console.log(postRet);
}

function cameraChange() {
	context.room.unsubscribe(context.localStream);
	context.room.unpublish(context.localStream);
	context.room.disconnect();
	context.localStream.close();
	
	if (cameraArray.length > 1)
		camera = camera == 0 ? 1 : 0;
	console.log("cameraChange: ", camera);
	streamInit();
}

// 初始化licode相关内容
function requestToken() {
	console.log("requestToken");
	postData({
		Role: "presenter",
		UserId: userId,
		IMEI: getIMEI(),
		Roomname: userId,
		Location: getCurrentLocation(),
		Longitude: getLongitude(),
		Latitude: getLatitude()
	}, baseUrl + "rest/requestToken");
	console.log("requestToken: ", postRet);
	context.token = postRet.token;
}

function streamInit() {
	//切换摄像头
	context.localStream = Erizo.Stream({audio:true,video: //true,
			{
				mandatory:{ minWidth:480, minHeight:480, maxWidth:480, maxHeight:480 },
				optional: [{ sourceId: cameraArray[camera] }]
			},
			data: true, videoSize: [480, 480, 480, 480],
		}
	);
	console.log("cameraArray[camera]",cameraArray);
	console.log("==========22222", context);
	context.localStream.init();
	context.localStream.addEventListener("access-accepted", function () {
		var subscribeToStreams = function (streams) {
			console.log("==========subscribeToStreams", streams);
			for (var index in streams) {
				var stream = streams[index];
				if (context.localStream.getID() !== stream.getID()) {
					context.room.subscribe(stream);
				}
			}
		};
		
		context.room.addEventListener("room-connected", function (roomEvent) {
			console.log("==========room-connected", context);
			context.room.publish(context.localStream);
			subscribeToStreams(roomEvent.streams);
		});

		context.room.addEventListener("stream-subscribed", function(streamEvent) {
			console.log("==========stream-subscribed", streamEvent);
		});

		context.room.addEventListener("stream-added", function (streamEvent) {
			console.log("==========stream-added", streamEvent);
			var streams = [];
			streams.push(streamEvent.stream);
			subscribeToStreams(streams);
		});

		context.room.startRecording(streamEvent.stream, function(recordingId, error) {
			if (recordingId === undefined){
				console.log("Error", error);
			} else {
				console.log("Recording started, the id of the recording is ", recordingId);
			}
		});
		
		context.room.addEventListener("stream-removed", function (streamEvent) {
			console.log("==========stream-removed", streamEvent);
			context.room.unpublish(streamEvent.stream);
		});
		console.log("==========33333", context);
		context.room.connect();
		context.localStream.show("myVideo");
		context.room.startRecording(context.localStream,function(recordingId) {
			console.log("recordingId---"+recordingId);
		});
	});
}

//发布直播
function publishLiving() {
	requestToken();
	context.room = Erizo.Room({token: context.token});
	streamInit();
	postData({
		UserId: userId,
		Token: context.token,
		Title: document.getElementById("publish_title").value,
		Describe: document.getElementById("publish_describe").value,
		Class: document.getElementById("publish_class").value,
		Tag: document.getElementById("publish_tag").value,
		Location: getCurrentLocation(),
		Longitude: getLongitude(),
		Latitude: getLatitude()
	}, baseUrl + "rest/item/publish");
	console.load(postRet);
}