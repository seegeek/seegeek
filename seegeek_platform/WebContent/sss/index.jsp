<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
	<meta name="viewport" content="width=device-width, user-scalable=yes, initial-scale=1, maximum-scale=1">
	<meta name="mobile-web-app-capable" content="yes">
	
    <title>SeeGeek</title>
	<link rel="stylesheet" href="http://58.53.219.69:8081/seegeek/sss/css/jquery.mobile-1.4.5.css" />
														
	<script type="text/javascript" src="http://58.53.219.69:8081/seegeek/sss/js/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="http://58.53.219.69:8081/seegeek/sss/js/erizo.js"></script>
	<script type="text/javascript" src="http://58.53.219.69:8081/seegeek/sss/js/and.js"></script>
	<script type="text/javascript" src="http://58.53.219.69:8081/seegeek/sss/js/common.js"></script>
	<script type="text/javascript" src="http://58.53.219.69:8081/seegeek/sss/js/crypt.js"></script>
	<script type="text/javascript" src="http://58.53.219.69:8081/seegeek/sss/js/main.js"></script>
	<script type="text/javascript" src="http://58.53.219.69:8081/seegeek/sss/js/show.js"></script>
	<script type="text/javascript" src="http://58.53.219.69:8081/seegeek/sss/js/user.js"></script>
	<script type="text/javascript" src="http://58.53.219.69:8081/seegeek/sss/js/media.js"></script>
</head>
<body onload="indexLoad()">
<div width=300 height=400><img id='ig' src=''/></div>
<hr/>
	<div>
		<div id="myVideo" style="width: 240px; height: 240px;"></div>
		<button id="selectCamera" type="button" onClick="cameraChange()">切换摄像头</button>
	</div>
	<div id="show"> </div>
	<table border="1">
		<tr>
			<td>
				<input id="username_login" type="text" maxlength=11 placeholder="用户名" />
				<input id="password_login" type="password" placeholder="密码" />
			</td>
			<td>
				<button id="btn_login" type="button" onClick="userLogin()">登陆</button>
			</td>
		</tr>
		<tr>
			<td>
				<input id="verification" type="text" maxlength=6 placeholder="验证码" />
			</td>
			<td>
				<button id="btn_getVerification" type="button" onClick="getVerification()">获取验证码</button>
			</td>
		</tr>
		<tr>
			<td>
				<input id="username_regist" type="text" maxlength=11 placeholder="用户名" />
				<input id="password_regist" type="password" placeholder="密码" />
			</td>
			<td>
				<button id="btn_regist" type="button" onClick="userRegist()">注册</button>
			</td>
		</tr>
		<tr>
			<td>
				<select id="upkey" onchange="updateKey = this.value">
					<option value="Nickname">昵称</option>
					<option value="Passwd">密码</option>
					<option value="Personal_signature">签名</option>
					<option value="Email">邮箱</option>
					<option value="Home_address">家乡</option>
					<option value="Work_address">地址</option>
					<option value="Icon">头像</option>
					<option value="Sex">性别</option>
				</select>
				<input id="upvalue" type="text" placeholder="新的信息" />
			</td>
			<td>
				<button id="btn_update" type="button" onClick="update()">更新</button>
			</td>
		</tr>
		<tr>
			<td>
				<input id="username_target" type="text" maxlength=11 placeholder="用户名" />
				<button id="btn_getPublishedList" type="button" onClick="getPublishedList()">获取发布历史</button>
			</td>
			<td>
				<button id="btn_getWatchedList" type="button" onClick="getWatchedList()">获取观看历史</button>
			</td>
		</tr>
		<tr>
			<td>
				<select id="sorttag" onchange="sortTag = this.value">
					<option value=1>热度</option>
					<option value=2>收藏</option>
					<option value=3>获赞</option>
				</select>
				<select id="offset_get" onchange="offsetGet = this.value">
					<option value=0>0</option>
					<option value=3>3</option>
					<option value=6>6</option>
				</select>
				<select id="count_get" onchange="countGet = this.value">
					<option value=3>3</option>
					<option value=5>5</option>
					<option value=10>10</option>
				</select>
			</td>
			<td>
				<button id="btn_getItemList" type="button" onClick="getItemList()">获取节目列表</button>
			</td>
		</tr>
		<tr>
			<td>
				<input id="keyword" type="text" placeholder="海底世界" />
				<select id="offset_search" onchange="offsetSearch = this.value">
					<option value=0>0</option>
					<option value=3>3</option>
					<option value=6>6</option>
				</select>
				<select id="count_search" onchange="countSearch = this.value">
					<option value=3>3</option>
					<option value=5>5</option>
					<option value=10>10</option>
				</select>
			</td>
			<td>
				<button id="btn_getSearchList" type="button" onClick="getSearchList()">搜素</button>
			</td>
		</tr>
		<tr>
			<td>
				<button id="btn_getItem" type="button" onClick="getItem()">获取节目信息</button>
			</td>
			<td>
				<button id="btn_getComment" type="button" onClick="getComment()">获取评论</button>
			</td>
		</tr>
		<tr>
			<td>
				<button id="btn_care" type="button" onClick="care('12343232423')">关注</button>
				<button id="btn_collect" type="button" onClick="collect()">收藏</button>
				<button id="btn_parise" type="button" onClick="parise()">赞</button>
				<button id="btn_report" type="button" onClick="report()">举报</button>
				<button id="btn_getCared" type="button" onClick="getCared()">关注的人</button>
				<button id="btn_getFansList" type="button" onClick="getFansList()">粉丝</button>
			</td>
		</tr>
		<tr>
			<td>
				<input id="comment_content" type="text" maxlength=32 placeholder="请输入评论内容" />
			</td>
			<td>
				<button id="btn_comment" type="button" onClick="comment()">评论</button>
			</td>
		</tr>
		<tr>
			<td>
				<input id="publish_title" type="text" placeholder="标题" />
				<input id="publish_describe" type="text" placeholder="描述" />
			</td>
			<td>
				<button id="btn_publishLiving" type="button" onClick="publishLiving()">发布视频</button>
			</td>
		</tr>
	</table>
</body>
</html>
