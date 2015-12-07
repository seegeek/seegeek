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
//获取节目
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


//点赞

//页面加载 js 方法，做一些初始化的准备工作
  $(function(){
	//执行获取赞数量的方法
   getPraiseNum();

  });
//js 函数
//注意: 函数中url 要用../rest,不然可能找不到 rest 接口地址
//获取赞的函数
function getPraiseNum()
{
 $.ajax({  
                url: '../rest/item/getPraiseNum',  
                type: 'GET',
                    data : {
				ItemId: 1
				},
			success : function(data, textStatus, jqXHR) {
				$("#PraiseNum").html(+data);
  //可以用jquery  $("#test").val(data) 方法来设置 赞的数量
			},
			dataType : "json"
                });  
}
//赞动作  的函数
function praise()
{
 $.ajax({  
 
                url: '../rest/item/praise',  
                type: 'POST',
                    data : {
				ItemId: 1,
				B: true
				},
			success : function(data, textStatus, jqXHR) {
				  var currentVal= $("#PraiseNum").html();
				  $("#PraiseNum").html(parseInt(currentVal)+1);
				//alert("返回状态--"+data);
				alert("已赞--");
			},
			dataType : "json"
                });  
}


//收藏

//页面加载 js 方法，做一些初始化的准备工作
  $(function(){
	//执行获取 数量的方法
   getCollectNum();

  });
//js 函数
//注意: 函数中url 要用../rest,不然可能找不到 rest 接口地址
//获取  的函数
function getCollectNum()
{
 $.ajax({  
                url: '../rest/item/getCollectNum',  
                type: 'GET',
                    data : {
				ItemId: 1
				},
			success : function(data, textStatus, jqXHR) {
				$("#LikeNum").html(+data);
  //可以用jquery  $("#test").val(data) 方法来设置 赞的数量
			},
			dataType : "json"
                });  
}
//  动作  的函数
function collect()
{
 $.ajax({  
 
                url: '../rest/item/collect',  
                type: 'POST',
                    data : {
				ItemId: 1,
				B: true
				},
			success : function(data, textStatus, jqXHR) {
				alert("已收藏--");
				  var currentVal= $("#LikeNum").html();
				  $("#LikeNum").html(parseInt(currentVal)+1);
				
				
			},
			dataType : "json"
                });  
}


// 评论
//页面加载 js 方法，做一些初始化的准备工作
  $(function(){
	//执行获取 数量的方法
   getCommentNum();

  });
//js 函数
//注意: 函数中url 要用../rest,不然可能找不到 rest 接口地址
//获取  的函数
function getCommentNum()
{
 $.ajax({  
                url: '../rest/item/getCommentNum',  
                type: 'GET',
                    data : {
				ItemId: 1
				},
			success : function(data, textStatus, jqXHR) {
				
				$("#CommentNum").html(+data);
  //可以用jquery  $("#test").val(data) 方法来设置 赞的数量
			},
			dataType : "json"
                });  
}
//  动作  的函数
function comment()
{
 $.ajax({  
                url: '../rest/item/comment',  
                type: 'POST',
                    data : {
				ItemId: 1,
				B: true
				},
			success : function(data, textStatus, jqXHR) {
			alert("已评论--");
			},
			dataType : "json"
                });  
}


//举报

//页面加载 js 方法，做一些初始化的准备工作
  //$(function(){
	//执行获取赞数量的方法
 //  getReport();

 // });
//js 函数
//注意: 函数中url 要用../rest,不然可能找不到 rest 接口地址
//获取  的函数
function getReportNum()
{
 $.ajax({  
                url: '../rest/item/getReportNum',  
                type: 'GET',
                    data : {
				ItemId: 1,
				B: true,
				},
			success : function(data, textStatus, jqXHR) {
				
				$("#ReportNum").html(+data);
  //可以用jquery  $("#test").val(data) 方法来设置 赞的数量
			},
			dataType : "json"
                });  
}
//  动作  的函数
function report()
{
 $.ajax({  
 
                url: '../rest/item/report',  
                type: 'POST',
                    data : {
				ItemId: 1,
				B: true
				},
			success : function(data, textStatus, jqXHR) {
				alert("已举报--");
			},
			dataType : "json"
                });  
}

//关注




//js 函数
//注意: 函数中url 要用../rest,不然可能找不到 rest 接口地址
//获取  的函数
function getSeeingNum()
{
 $.ajax({  
                url: '../rest/item/getSeeingNum',  
                type: 'GET',
                    data : {
				UserId: 1,
				B: true,
				},
			success : function(data, textStatus, jqXHR) {
				$("#SeeingNum").html(+data);
  //可以用jquery  $("#test").val(data) 方法来设置 赞的数量
			},
			dataType : "json"
                });  
}



//js 函数
//注意: 函数中url 要用../rest,不然可能找不到 rest 接口地址
//获取  的函数
function getSaw()
{
 $.ajax({  
                url: '../rest/item/getSawNum',  
                type: 'GET',
                    data : {
					  ItemId: 1,
					  B: true,
				},
			success : function(data, textStatus, jqXHR) {
				$("#SawNum").html(+data);
  //可以用jquery  $("#test").val(data) 方法来设置 赞的数量
			},
			dataType : "json"
                });  
}



// title; tag.....获取值

function publish()
{
 $.ajax({  
                url: '../rest/item/publish',  
                type: 'POST',  
         		data : {
						ItemId: 1,
				         B: true,
						title:Title,
						tag:Tag,
						location:Location,
						nikename: Nikename,
				
			},
			success : function(data, textStatus, jqXHR) {
				//alert("更新个人信息--"+data);
			},
			dataType : "json"
                });  
}

//获取  的函数
function getItem()
{
 $.ajax({  
                url: '../rest/item/getItem',  
                type: 'GET',
                    data : {
					ItemId: 1,
				    B: true,
				},
			success : function(data, textStatus, jqXHR) {
				$("#Title").html(data.title);
				$("#Tag").html("#"+data.tag);
				$("#SeeingNum").html(data.seen_num+"人");
				$("#Location").html(data.location);
  //可以用jquery  $("#test").val(data) 方法来设置 赞的数量
			 },
			dataType : "json"
                });  
}



// 评论内容; 

function comment()
{
 $.ajax({  
               url: '../rest/item/comment',  
                type: 'POST',  
         		data : {
						ItemId:1,
						B: true,
						Content:"年后",
						
				
			},
			success : function(data, textStatus, jqXHR) {
				//alert("更新个人信息--"+data);
			},
			dataType : "json"
                });  
}

//页面加载 js 方法，做一些初始化的准备工作
  $(function(){
	//执行获取赞数量的方法
  // getComment();

  });

//获取  的函数
function getComment()
{

 $.ajax({  
                url: '../rest/item/getComment',  
                type: 'GET',  
                  data : {
				ItemId: 1,
				Offset: 1,
				Num :1
			},
			success : function(data, textStatus, jqXHR) {
				var obj = eval(data);
				for(var i=0;i<obj.length;i++)
				{
				$("#Content").append("	<div class='box'><span class='span' id='Nikename'>"+obj[i].userId+"</span><span>"+obj[i].content+"</span></div>");
				 }
			},
			dataType : "json"
                });  
 
}
function initCamera() {
	if (typeof MediaStreamTrack === 'undefined') {
		alertToast('This browser does not support MediaStreamTrack.\n\nTry Chrome.');
	} else {
		MediaStreamTrack.getSources(function (sourceInfos) {
			cameraArray = [];
			console.log("sourceInfos:%o", sourceInfos);
			for (var i = 0; i != sourceInfos.length; ++i) {
				var sourceInfo = sourceInfos[i];
				if (sourceInfo.kind === 'video') {
					console.log("get camera ", sourceInfo.id);
					cameraArray.push(sourceInfo.id);
				}
			}
		});
	}
}

function cameraChange() {
	context.room.unsubscribe(context.localStream);
	context.room.unpublish(context.localStream);
	context.room.disconnect();
	context.localStream.close();
	
	if (cameraArray.length > 1)
		camera = camera == 0 ? 1 : 0;
	console.log("cameraChange: ", camera);
	publishLiving();
}

// 初始化licode相关内容
function requestToken() {
	console.log("requestToken");
	postData({
		Role: "presenter",
		UserId: getIMEI(),
		IMEI: getIMEI(),
		RoomName: getIMEI(),
		Location: getCurrentLocation(),
		Longitude: getLongitude(),
		Latitude: getLatitude()
	}, baseUrl + "rest/requestToken");
	console.log("requestToken: ", postRet);
	context.token = postRet.token;
}

//发布直播
function publishLiving() {
	requestToken();
	context.room = Erizo.Room({token: context.token});
	initCamera();
	streamInit();
	postData({
		UserId: 15010215479,
		Token: context.token,
		Title: document.getElementById("publish_title").value,
		Describe: document.getElementById("publish_describe").value,
		Class: 1,
		Tag: "运动",
		Location: getCurrentLocation(),
		Longitude: getLongitude(),
		Latitude: getLatitude()
	}, baseUrl + "rest/item/publish");
	console.log(postRet);
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
			
			context.room.startRecording(streamEvent.stream, function(recordingId, error) {
				if (recordingId === undefined){
					console.log("Error", error);
				} else {
					console.log("Recording started, the id of the recording is ", recordingId);
				}
			});
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
