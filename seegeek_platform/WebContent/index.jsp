<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	    <script type="text/javascript" src="${basePath }Js/jquery.js"></script>
	    <script type="text/javascript" src="${basePath }seegeek.js"></script>
  </head>
  <body>
  <table border="1" >
  <tr><td>获取验证码</td><td>  <input type="button" onclick="getVerification()" value="getVerification"/></td><td>完成</td></tr>
  <tr><td>注册用户接口</td><td>  <input type="button" onclick="register()" value="register"/></td><td>完成</td></tr>
  <tr><td>验证密码</td><td>  <input type="button" onclick="verify()" value="verify"/></td><td>完成</td></tr>
  <tr><td>修改信息</td><td>  <input type="button" onclick="update()" value="update"/></td><td>完成</td></tr>
  <tr><td>获取用户信息</td><td>  <input type="button" onclick="getInfo()" value="getInfo"/></td><td>完成</td></tr>
  <tr><td>获取历史发布信息</td><td>  <input type="button" onclick="getPublishedList()" value="getPublishedList"/></td><td>完成</td></tr>
  <tr><td>获取观看历史</td><td>  <input type="button" onclick="getWatchedList()" value="getWatchedList"/></td><td>完成</td></tr>
  <tr><td>获取观看历史数量</td><td>  <input type="button" onclick="getWatchedNum()" value="getWatchedNum"/></td><td>完成</td></tr>
  </table>
<hr/>
  <table border="1">
  <tr><td>获取getItemListEntity</td><td>  <input type="button" onclick="getItemListEntity()" value="getItemListEntity"/></td><td>完成</td></tr>
  <tr><td>获取节目列表</td><td>  <input type="button" onclick="getItemList()" value="getItemList"/></td><td>完成</td></tr>
  <tr><td>搜索</td><td>  <input type="button" onclick="getSearchList()" value="getSearchList"/></td><td>完成</td></tr>
  </table>
  <hr/>
  <table border="1">
  <tr><td>收看直播</td><td>  <input type="button" onclick="getRoomId()" value="getRoomId"/></td><td>完成</td></tr>
  </table>
  <hr/>
  <table border="1">
  <tr><td>获取节目信息</td><td>  <input type="button" onclick="getItem()" value="getItem"/></td><td>完成</td></tr>
  <tr><td>获取评论</td><td>  <input type="button" onclick="getComment()" value="getComment"/></td><td>完成</td></tr>
  </table>
  <hr/>
  <table border="1">
  <tr><td>关注</td><td>  <input type="button" onclick="care()" value="care"/></td><td>完成</td></tr>
  <tr><td>获取粉丝列表</td><td>  <input type="button" onclick="getFansList()" value="getFansList"/></td><td>完成</td></tr>
  <tr><td>获取关注列表</td><td>  <input type="button" onclick="getCareList()" value="getCareList()"/></td><td>完成</td></tr>
  <tr><td>获取getCaredItemList</td><td>  <input type="button" onclick="getCaredItemList()" value="getCaredItemList()"/></td><td>完成</td></tr>
  </table>
  <hr/>
  <table border="1">
  <tr><td>收藏节目</td><td>  <input type="button" onclick="collect()" value="collect"/></td><td>完成</td></tr>
  <tr><td>收藏获取量</td><td>  <input type="button" onclick="getCollectNum()" value="getCollectNum"/></td><td>完成</td></tr>
  <tr><td>点赞</td><td>  <input type="button" onclick="parise()" value="parise"/></td><td>完成</td></tr>
  <tr><td>点赞节目总量</td><td>  <input type="button" onclick="getPraiseNum()" value="getPraiseNum()"/></td><td>完成</td></tr>
  <tr><td>评论节目</td><td>  <input type="button" onclick="comment()" value="comment()"/></td><td>完成</td></tr>
  <tr><td>评论节目总量</td><td>  <input type="button" onclick="getCommentNum()" value="getCommentNum"/><td>完成</td></tr>
  <tr><td>举报</td><td>  <input type="button" onclick="report()" value="report"/></td><td>完成</td></tr>
  <tr><td>获取某个节目的举报总量</td><td>  <input type="button" onclick="getReportNum()" value="getReportNum"/></td><td>完成</td></tr>
  <tr><td>访问动作</td><td>  <input type="button" onclick="saw()" value="saw"/></td><td>完成</td></tr>
  <tr><td>查看节目访问量</td><td>  <input type="button" onclick="getSawNum()" value="getSawNum"/></td><td>完成</td></tr>
  </table>  
  <hr/>
  <form id="form" name="form" action="rest/item/upload" method="post" >
    <table border="1">
  <tr><td>请求token</td><td>  <input type="button" onclick="requestToken()" value="requestToken"/></td><td>完成</td></tr>
  <tr><td>发布节目</td><td>  <input type="button" onclick="publish()" value="publish"/></td></tr>
  <tr><td>上传</td><td>  <input type="file" name="file"  value="浏览"/>  <input type="button" onclick="upload()" value="upload"/></td></tr>
  <tr><td>checkLoginStatus</td><td>  <input type="button" onclick="checkLoginStatus()" value="checkLoginStatus"/></td></tr>
  <tr><td>getItemSource</td><td>  <input type="button" onclick="getItemSource()" value="getItemSource"/></td></tr>
  <tr><td>getPublicItemListEntity </td><td>  <input type="button" onclick="getPublicItemListEntity()" value="getPublicItemListEntity "/></td></tr>
  <tr><td>careLocaiton </td><td>  <input type="button" onclick="careLocation()" value="careLocation "/></td></tr>
  <tr><td>getAroundListItem</td><td>  <input type="button" onclick="getAroundListItem()" value="getAroundListItem "/></td></tr>
  <tr><td>激活邮箱</td><td>  <input type="button" onclick="bind_email()" value="bind_email "/></td></tr>
  <tr><td>注册接口new</td><td>  <input type="button" onclick="registerInfo()" value="registerInfo "/></td></tr>
  <tr><td>上传视频</td><td>  <input type="button" onclick="uploadVideo()" value="uploadVideo"/></td></tr>
  <tr><td>test</td><td>  <input type="button" onclick="test()" value="test"/></td></tr>
  </table>
  </form>
  </body>
</html>
