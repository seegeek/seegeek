<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
 <head>
<%@ include file="/WEB-INF/views/Public/commons.jspf" %>
    <title>后台管理系统</title>
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
        content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="mobile-web-app-capable" content="yes">
<link rel="icon" sizes="192x192" href="assets/i/app-icon72x72@2x.png">
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
   	<link rel="stylesheet" type="text/css" href="${basePath }Css/bootstrap.css" />
    <link rel="stylesheet" type="text/css" href="${basePath }Css/bootstrap-responsive.css" />
    <link rel="stylesheet" type="text/css" href="${basePath }Css/style.css" />
    <script type="text/javascript" src="${basePath }Js/jquery.js"></script>
    <script type="text/javascript" src="${basePath }Js/jquery.sorted.js"></script>
    <script type="text/javascript" src="${basePath }Js/bootstrap.js"></script>
    <script type="text/javascript" src="${basePath }Js/ckform.js"></script>
    <script type="text/javascript" src="${basePath }Js/common.js"></script>
    <style type="text/css">
        body {
            padding-top: 40px;
            padding-bottom: 40px;
            background-color: #f5f5f5;
        }

        .form-signin {
            max-width: 400px;
            padding: 19px 29px 29px;
            margin: 0 auto 20px;
            background-color: #fff;
            border: 1px solid #e5e5e5;
            -webkit-border-radius: 5px;
            -moz-border-radius: 5px;
            border-radius: 5px;
            -webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
            -moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
            box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
        }

        .form-signin .form-signin-heading,
        .form-signin .checkbox {
            margin-bottom: 10px;
        }

        .form-signin input[type="text"],
        .form-signin input[type="password"] {
            font-size: 16px;
            height: auto;
            margin-bottom: 15px;
            padding: 7px 9px;
        }

    </style>  
</head>
<body>
<div class="container">
    <form class="form-signin" method="post" action="op/SysUserAction.do?method=loginByUser">
    <input type="hidden" name="mode" id="mode"/>
        <h2 class="form-signin-heading"><br></h2><h2 class="form-signin-heading">极视科技运营系统V1.0</h2>
       <div id="login1"> <input type="text" name="username" id="username"  class="input-block-level" placeholder="账号">
        <input type="password" name="passwd" id="passwd"  class="input-block-level" placeholder="密码">
 		 <p><input type="text" name="randCode" id="randCode" class="input-medium" placeholder="手机验证码">     <input name="btnSendCode"   type="button" style="height: 36px;" class="btn btn-large" value="发送验证码" onclick="sendMessage()" /> </p>
         <!-- <input type="text" name="randCode" id="randCode" class="input-medium" placeholder="验证码"/>    
        <img id="imgObj" alt="验证码" src="RandomCode" />
        <a href="#" onclick="changeImg()">换一张</a>-->
        <p><a href="${basePath}login2.jsp" onclick="changeMode(2)">手机登录</a> <button class="btn btn-large btn-primary" type="submit" onclick="return loginByUser();">登录</button></p>
  	</div>
  	
    </form>
</div>
</body>
</html>
<script type="text/javascript">
$(function(){
document.getElementById("username").autocomplete="off";
document.getElementById("passwd").autocomplete="off";
});
function loginByUser()
{
var username=document.getElementById("username").value;
var passwd=document.getElementById("passwd").value;
var randCode=document.getElementById("code1").value;

}
function loginByPhone()
{
var mobilephone=document.getElementById("mobilephone").value;
var randCode=document.getElementById("code2").value;
var sign;
 $.ajax({  
                url: 'op/SysUserAction.do?method=loginByPhone',  
                type: 'GET',  
                async:false,
         		data : {
				Mobilephone: mobilephone,
				randCode :randCode
			},
			success : function(data, textStatus, jqXHR) {
			sign=data;
			},
			dataType : "json"
                });  
if(sign==0)
			{
			return true;
			}
			else
			{
			alert("请重新输入正确的信息!");
			return false;
			}
}


function changeImg() {
        var imgSrc = $("#imgObj");
        var src = imgSrc.attr("src");
        imgSrc.attr("src", chgUrl(src));
    }
    //时间戳   
    //为了使每次生成图片不一致，即不让浏览器读缓存，所以需要加上时间戳   
    function chgUrl(url) {
        var timestamp = (new Date()).valueOf();
        url = url.substring(0, 17);
        if ((url.indexOf("&") >= 0)) {
            url = url + "×tamp=" + timestamp;
        } else {
            url = url + "?timestamp=" + timestamp;
        }
        return url;
    }
    
    
   
var InterValObj; //timer变量，控制时间  
var count = 120; //间隔函数，1秒执行  
var curCount;//当前剩余秒数  
var code = ""; //验证码  
var codeLength = 4;//验证码长度  
function sendMessage() {  
    curCount = count;  
    var phone=$("#phone").val();//手机号码  
    if(phone != ""){  
        //产生验证码  
        for (var i = 0; i < codeLength; i++) {  
            code += parseInt(Math.random() * 9).toString();  
        }  
        //设置button效果，开始计时  
        $("input[name='btnSendCode']").attr("disabled", "true");  
        $("input[name='btnSendCode']").val("发送验证码("+curCount+")");
        InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次  
    //向后台发送处理数据  
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
    }else{  
        alert("手机号码不能为空！");  
    }  
}  
//timer处理函数  
function SetRemainTime() {  
var mode=$("#mode").val();
    if (curCount == 0) {                  
        window.clearInterval(InterValObj);//停止计时器  
 
  	$("input[name='btnSendCode']").removeAttr("disabled");//启用按钮  
    $("input[name='btnSendCode']").val("重新发送验证码");  
       
        code = ""; //清除验证码。如果不清除，过时间后，输入收到的验证码依然有效      
    }  
    else {  
        curCount--;  
        

    $("input[name='btnSendCode']").val("发送验证码("+curCount+")");  
          
    }  
}      
    
    
</script>