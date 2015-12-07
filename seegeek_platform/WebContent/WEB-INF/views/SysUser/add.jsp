<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
 <head>
 <%@ include file="/WEB-INF/views/Public/commons.jspf" %>
    <title></title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${basePath }Css/bootstrap.css" />
    <link rel="stylesheet" type="text/css" href="${basePath }Css/bootstrap-responsive.css" />
    <link rel="stylesheet" type="text/css" href="${basePath }Css/style.css" />
    <script type="text/javascript" src="${basePath }Js/jquery.js"></script>
    <script type="text/javascript" src="${basePath }Js/bootstrap.js"></script>
    <style type="text/css">
        body {
            padding-bottom: 40px;
        }
        .sidebar-nav {
            padding: 9px 0;
        }

        @media (max-width: 980px) {
            /* Enable use of floated navbar text */
            .navbar-text.pull-right {
                float: none;
                padding-left: 5px;
                padding-right: 5px;
            }
        }


    </style>
</head>
<body>
<form action="SysUserAction.do?method=add" method="post" class="definewidth m20">
<table class="table table-bordered table-hover definewidth m10">
  <tr>
        <td class="tableleft">组织</td>
        <td>
        <select name="departmentId">
		<c:forEach var="r" items="${departmentList}">
		<option value="${r.id }">${r.name }</option>
		</c:forEach>
        </select>
        </td>
    </tr>
    <tr>
        <td width="10%" class="tableleft">登录名</td>
        <td><input type="text" name="loginName" id="loginName"/></td>
    </tr>
     <tr>
        <td class="tableleft">密码</td>
        <td><input type="text" name="passwd" id="passwd"/></td>
    </tr>
    <tr>
        <td class="tableleft">邮箱</td>
        <td><input type="text" name="email" id="email"/></td>
    </tr>
    <tr>
        <td class="tableleft">电话</td>
        <td><input type="text" name="mobilePhone" id="mobilePhone"/></td>
    </tr>
    <tr>
        <td class="tableleft"> <input id="btnSendCode" type="button" value="发送验证码" onclick="sendMessage()" /> </td>
        <td><input type="text" name="message_code" id="message_code"/></td>
    </tr>
    <tr>
        <td class="tableleft">性别</td>
        <td>
         <input type="radio" name="sex" value="1" checked/> 女
         <input type="radio" name="sex" value="0"/> 男
        </td>
    </tr>
    <tr>
        <td class="tableleft">角色</td>
        <td>
        <select name="roleIds" multiple="multiple">
		<c:forEach var="r" items="${roleList}">
		<option value="${r.id }">${r.name }</option>
		</c:forEach>
        </select>
        </td>
    </tr>
    <tr>
        <td class="tableleft"></td>
        <td>
            <button type="submit" class="btn btn-primary" type="button">保存</button> &nbsp;&nbsp;<button type="button" class="btn btn-success" name="backid" id="backid">返回列表</button>
        </td>
    </tr>
</table>
</form>
</body>
</html>
<script>
    $(function () {       
		$('#backid').click(function(){
				window.location.href="index.html";
		 });

    });
    
    
    
var InterValObj; //timer变量，控制时间  
var count = 120; //间隔函数，1秒执行  
var curCount;//当前剩余秒数  
var code = ""; //验证码  
var codeLength = 4;//验证码长度  
function sendMessage() {  
check();
}  
//timer处理函数  
function SetRemainTime() {  
    if (curCount == 0) {                  
        window.clearInterval(InterValObj);//停止计时器  
        $("#btnSendCode").removeAttr("disabled");//启用按钮  
        $("#btnSendCode").val("重新发送验证码");  
        code = ""; //清除验证码。如果不清除，过时间后，输入收到的验证码依然有效      
    }  
    else {  
        curCount--;  
        $("#btnSendCode").val("请在" + curCount + "秒内输入验证码");  
    }  
}  


function time_code()
{


 curCount = count;  
    var phone=$("#mobilePhone").val();//手机号码  
    if(phone != ""){  
        //产生验证码  
        for (var i = 0; i < codeLength; i++) {  
            code += parseInt(Math.random() * 9).toString();  
        }  
        //设置button效果，开始计时  
        $("#btnSendCode").attr("disabled", "true");  
        $("#btnSendCode").val("请在" + curCount + "秒内输入验证码");  
        InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次  
    //向后台发送处理数据  
    $.ajax({  
                url: '../rest/getVerification',  
                type: 'GET',  
         		data : {
				UserId: $("#mobilePhone").val(),
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
function check()
{
 $.ajax({  
                url: '../rest/checkMobilePhone',  
                type: 'POST',  
         		data : {
				UserId: $("#mobilePhone").val(),
			},
			success : function(data, textStatus, jqXHR) {
				if(data===1)
				{
				alert("手机已被注册");
				}
				else if(data===0)
				{
				time_code();
				}
			},
			dataType : "json"
                });  

}
</script>