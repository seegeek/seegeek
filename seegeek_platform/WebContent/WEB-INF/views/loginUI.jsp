<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
 <head>
<%@ include file="/WEB-INF/views/Public/commons.jspf" %>
    <title>后台管理系统</title>
    
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
	   <script type="text/javascript" src="${basePath }Js/jquery.js"></script>
   	<link rel="stylesheet" type="text/css" href="${basePath }Css/bootstrap.css" />
    <link rel="stylesheet" type="text/css" href="${basePath }Css/bootstrap-responsive.css" />
    <link rel="stylesheet" type="text/css" href="${basePath }Css/style.css" />
 
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
    <form class="form-signin" method="post" action="op/UserAction.do?method=toMain">
        <h2 class="form-signin-heading"><br></h2><h2 class="form-signin-heading">极视科技运营系统V1.0</h2>
        <input type="text" name="username" id="username"  class="input-block-level" placeholder="账号">
        <input type="password" name="passwd" id="passwd"  class="input-block-level" placeholder="密码">
      <input type="text" name="randCode" id="randCode" class="input-medium" placeholder="验证码"/>    
        
         <img id="imgObj" alt="验证码" src="RandomCode" />
       	 <a href="#" onclick="changeImg()">换一张</a>
        <p><button class="btn btn-large btn-primary" type="submit" onclick="return login();">登录</button></p>
    </form>
</div>
</body>
</html>
<script type="text/javascript">
$(function(){
document.getElementById("username").autocomplete="off";
document.getElementById("passwd").autocomplete="off";
document.getElementById("randCode").autocomplete="off";
});
function login()
{
var username=document.getElementById("username").value;
var passwd=document.getElementById("passwd").value;
var sign;
 $.ajax({  
                url: 'op/UserAction.do?method=login',  
                type: 'GET',  
                async:false,
         		data : {
				username: username,
				passwd :passwd
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
			alert("用户或密码输入错误，请重新输入!");
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
</script>
