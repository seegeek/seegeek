function adduser()
{
 $.ajax({  
                    url: 'rest/adduser',  
                    type: 'POST',  
                    dataType: 'json',  
                    data: {id: 1,name:'张三',sex:0,age:23}  
                }).done(function(data, status, xhr) {  
                
                }).fail(function(xhr, status, error) {  
                alert(error);
                });  
}


function test()
{
 $.ajax({  
                    url: 'rest/item/test',  
                    type: 'POST',  
                    dataType: 'json',  
                    contentType: "application/json; charset=utf-8",
                    	data : JSON.stringify({
				Name: "zgf",
				Md5 :"d"
				}) ,
                   // data: JSON.stringify({name:"cehshi1",md5:'aldjladkjflakds'}),  
                }).done(function(data, status, xhr) {  
                
                }).fail(function(xhr, status, error) {  
                alert(error);
                });  
}
function getInfo()
{
 $.ajax({  
                url: 'rest/getInfo',  
                type: 'GET',  
			success : function(data, textStatus, jqXHR) {
				alert("异步方法提交"+data);
			},
			dataType : "json"
                });  
}
function getPublicItemListEntity()
{
 $.ajax({  
                url: 'rest/item/getPublicItemListEntity',  
                type: 'GET',  
			success : function(data, textStatus, jqXHR) {
				alert("异步方法提交"+data);
			},
			dataType : "json"
                });  
}

function getitemList()
{
 $.ajax({  
                url: 'rest/item/getItemList',  
                type: 'POST',  
         		data : {
				title: "这里是标题",
				content :"这里是内容"
			},
			success : function(data, textStatus, jqXHR) {
				alert("异步方法提交")
			},
			dataType : "json"
                });  
}
function bind_email()
{

 $.ajax({  
                url: 'rest/bind_email',  
                type: 'POST',  
         		data : {
				Email: "280125706@qq.com",
			},
			success : function(data, textStatus, jqXHR) {
				alert("异步方法提交")
			},
			dataType : "json"
                });  
}
function getCaredItemList()
{
 $.ajax({  
                url: 'rest/item/getCaredItemList',  
                type: 'GET',  
			success : function(data, textStatus, jqXHR) {
				alert("异步方法提交")
			},
			dataType : "json"
                });  
}
function getAroundListItem()
{
 $.ajax({  
                url: 'rest/item/getAroundItemList',  
                type: 'GET',  
                data:{Num:5,Offset:0},
			success : function(data, textStatus, jqXHR) {
				alert("异步方法提交")
			},
			dataType : "json"
                });  
}
function getVerification()
{
 $.ajax({  
                url: 'rest/getVerification',  
                type: 'GET',  
         		data : {
				UserId: 15010215479,
				IMEI :'1111111111111111'
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function getItemSource()
{
 $.ajax({  
                url: 'rest/item/getItemSource',  
                type: 'GET',  
         		data : {
				ItemId: 21
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function register()
{
 $.ajax({  
                url: 'rest/register',  
                type: 'POST',  
         		data : {
				UserId: 133331111,
				Verification: '12132',
				PasswordEn: 'salkflasjdkf',
				IMEI :'12hhhsds2fjlsk'
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function registerInfo()
{
 $.ajax({  
                url: 'rest/registerInfo',  
                type: 'POST',  
         		data : {
				UserId: 133331113,
				Verification: '12132',
				PasswordEn: 'salkflasjdkf',
				IMEI :'12hhhsds2fjlsk',
				Language :'CN',
				Device_os :'Apple',
				Device_os_version :'IOS9.0',
				Idfa :'iii-333-111-ooo',
				Device_token :'adasdfasdfasdfsf',
				Device_name :'zgf的iphone',
				Cpu :'inter x86',
				Device_unicode :'aksdlaskdkkkkkkkalallala',
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function careLocation()
{
 $.ajax({  
                url: 'rest/careLocation',  
                type: 'POST',  
         		data : {
				Longitude: 431,
				Latitude:241,
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}

function update()
{
var nickname='test';
var Email='280125706@qq.com';
var icon="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAIAAACRXR/mAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAADmSURBVFhH7dfBDcMgEERRHynBR0qhHDqhDMqgPILEKrJsHJgNiog175ZIIf+wZuUtL4lZCGYhmIVgFoJZCGYh4KyUkrV2w5VfxRjllB44S9dUGWPklB44K4Qgf4Lz3sspPRx5xOSs8kA458ZH+87krH3fywyNj/adyVl1tAv5rMUsBLMQD8pCtzW0pCtNlmJbozeZJkuxrceXdPWg2fpAopg1RKKY1VfuM4laKut4n8lXWjOz3vcZektdzcyaqJGlfkFVuy7NRtaPm6rT0mxkffOCqnYax/+ZrRUwC8EsBLMQS2bl/AJ8BPYV/soUCwAAAABJRU5ErkJggg==";
updateCommon(nickname,null,null,null,Email,null,null,icon,null);
}


function updateNickName()
{
var nickname=document.getElementById("Nickname").value;
updateCommon(nickname,null,null,null,Email,null,null,null,null);
}
function updateMobilePhone()
{
var mobilePhone=document.getElementById("mobilePhone").value;
updateCommon(null,mobilePhone,null,null,Email,null,null,null,null);
}

function updateCommon(p1,p2,p3,p4,p5,p6,p7,p8,p9)
{
 $.ajax({  
                url: 'rest/update',  
                type: 'POST',  
         		data : {
				Nickname: p1,
				MobilePhone: p2,
				Passwd :p3,
				Personal_signature :p4,
				Email :p5,
				Home_address :p6,
				Work_address :p7,
				Icon :p8,
				Sex :p9,
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			error: function (e) {
			alert(e);
			
			},
			dataType : "json"
                });  
}
function verify()
{
 $.ajax({  
                url: 'rest/verify',  
                type: 'POST',  
         		data : {
				UserId: '280125706@qq.com',
				PasswordEn: 'Q1w2e3r4',
				IMEI :'1111111111111111'
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function checkLoginStatus()
{
 $.ajax({  
                url: 'rest/checkLoginStatus',  
                type: 'POST',  
         		data : {
				UserId: 15010215479
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function getPublishedList()
{
 $.ajax({  
                url: 'rest/item/getPublishedList',  
                type: 'GET',  
         		data : {
				UserId: 1
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data[0].id);
			},
			dataType : "json"
                });  
}
function getWatchedList()
{
 $.ajax({  
                url: 'rest/item/getWatchedList',  
                type: 'GET',  
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data[0].id);
			},
			dataType : "json"
                });  
}
function getWatchedNum()
{
 $.ajax({  
                url: 'rest/item/getWatchedNum',  
                type: 'GET',  
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function getItemList()
{
 $.ajax({  
                url: 'rest/item/getItemList',  
                type: 'GET',  
                data : {
				SortTag: 1,
				Offset: 1,
				Num :1
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data[0].id);
			},
			dataType : "json"
                });  
}
function getItemListEntity()
{
 $.ajax({  
                url: 'rest/item/getItemListEntity',  
                type: 'GET',  
                data : {
				SortTag: 1,
				Offset: 1,
				Num :1
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function getRoomId ()
{
 $.ajax({  
                url: 'rest/item/getRoomId',  
                type: 'GET',  
                  data : {
				ItemId: "1"
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data.roomId);
			},
			dataType : "json"
                });  
}
function getSearchList()
{
 $.ajax({  
                url: 'rest/item/getSearchList',  
                type: 'GET',  
                data : {
				Keyword: "111",
				Offset: 1,
				Num :1
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data[0].id);
			},
			dataType : "json"
                });  
}
function getItem()
{
 $.ajax({  
                url: 'rest/item/getItem',  
                type: 'GET',  
                  data : {
				ItemId :449
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function getComment()
{
 $.ajax({  
                url: 'rest/item/getComment',  
                type: 'GET',  
                  data : {
				ItemId: 1,
				Offset: 1,
				Num :1
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}

function care()
{
 $.ajax({  
                url: 'rest/item/care',  
                type: 'POST',  
                 data : {
				UserId: 1,
				B: true
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function saw()
{
 $.ajax({  
                url: 'rest/item/saw',  
                type: 'POST',  
                 data : {
				ItemId: 385,
				B: true
			},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function getSawNum()
{
 $.ajax({  
                url: 'rest/item/getSawNum',  
                type: 'GET',
                data:{ItemId:385},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function getFansList()
{
 $.ajax({  
                url: 'rest/item/getFansList',  
                type: 'GET',
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function getCareList()
{
 $.ajax({  
                url: 'rest/item/getCareList',  
                type: 'GET',
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function getFansList()
{
 $.ajax({  
                url: 'rest/item/getFansList',  
                type: 'GET',
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function collect()
{
 $.ajax({  
                url: 'rest/item/collect',  
                type: 'POST',
       data : {
				ItemId: 1,
				B: true
				},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function getCollectNum()
{
 $.ajax({  
                url: 'rest/item/getCollectNum',  
                type: 'GET',
                    data : {
				ItemId: 1
				},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function praise()
{
 $.ajax({  
                url: 'rest/item/praise',  
                type: 'POST',
                data : {
				ItemId: 1,
				B: true
				},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function getPraiseNum()
{
 $.ajax({  
                url: 'rest/item/getPraiseNum',  
                type: 'GET',
                    data : {
				ItemId: 1
				},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function comment()
{
 $.ajax({  
                url: 'rest/item/comment',  
                type: 'POST',
                    data : {
				ItemId: 1,
				B: true,
				Content:'赵高飞'
				},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function getCommentNum()
{
 $.ajax({  
                url: 'rest/item/getCommentNum',  
                type: 'GET',
                    data : {
				ItemId: 1
				},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function report()
{
 $.ajax({  
                url: 'rest/item/report',  
                type: 'POST',
                    data : {
				ItemId: 1,
				B: true
				},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function getReportNum()
{
 $.ajax({  
                url: 'rest/item/getReportNum',  
                type: 'GET',
                    data : {
				ItemId: 1
				},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function requestToken()
{
 $.ajax({  
                url: 'rest/requestToken',  
                type: 'POST',
                    data : {
				Role: "publisher",
				UserId:"15010215479",
				IMEI:"1111111111111111",
				RoomName:"aslkdjflksjflksda",
				Location:"北京海淀",
				Longitude:"233.22",
				Latitude:"233.11",
				},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function publish()
{
 $.ajax({  
                url: 'rest/item/publish',  
                type: 'POST',
                data : {
				UserId: "15010215479",
				Token:"kasdfjashdkjfdhasf",
				Title:"分",
				Describe:"AAAA",
				Classes:"oh my good",
				Tag:"alksdlas",
				Location:"西藏",
				Longitude:"223.11",
				Latitude:"113.22",
				RoomId:'asdasd',
				Role:'viewer',
				ItemId:27
				},
			success : function(data, textStatus, jqXHR) {
				alert("获取验证码--"+data);
			},
			dataType : "json"
                });  
}
function upload()
{

var data=document.getElementById("png").src;
$.ajax({  
                url: 'rest/item/uploadImg',  
                type: 'POST',
                 data : {
				ImgData:data,
				},
			success : function(data, textStatus, jqXHR) {
				alert("上传图片--"+data);
			},
			dataType : "json"
                });  
}

function uploadVideo()
{

$.ajax({  
                url: 'rest/item/uploadVideo',  
                type: 'POST',
                 data : {
				Name:'28b32ec7-b8bf-4bb7-a521-239342444792',
				Md5:'',
				TotalSize:''
				},
			success : function(data, textStatus, jqXHR) {
				alert("上传图片--"+data);
			},
			dataType : "json"
                });  
}
