<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
 <head>
 <%@ include file="/WEB-INF/views/Public/commons.jspf" %>
    <title>用户管理</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${basePath }Css/bootstrap.css" />
    <link rel="stylesheet" type="text/css" href="${basePath }/Css/bootstrap-responsive.css" />
    <link rel="stylesheet" type="text/css" href="${basePath }/Css/style.css" />
    <script type="text/javascript" src="${basePath }Js/jquery.js"></script>
    <script type="text/javascript" src="${basePath }Js/jquery.sorted.js"></script>
    <script type="text/javascript" src="${basePath }Js/bootstrap.js"></script>
    <script type="text/javascript" src="${basePath }Js/ckform.js"></script>
    <script type="text/javascript" src="${basePath }Js/common.js"></script>

 
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
<form class="form-inline definewidth m20" action="index.html" method="get">    
    用户名称：
    <input type="text" name="username" id="username"class="abc input-default" placeholder="" value="">&nbsp;&nbsp;  
    <button type="submit" class="btn btn-primary">查询</button>&nbsp;&nbsp; <button type="button" class="btn btn-success" id="addnew">新增用户</button>
</form>
<table class="table table-bordered table-hover definewidth m10">
    <thead>
    <tr>
        <th>ID</th>
        <th>直播标题</th>
        <th>直播标签</th>
        <th>直播描述</th>
        <th>地理位置</th>
        <th>类型</th>
        <th>直播时间</th>
        <th>操作</th>
    </tr>
    </thead>
        <c:forEach var="entity" items="${entityList}">
          <tr>
            <td>${entity.id }</td>
            <td>${entity.title }</td>
            <td>${entity.tag }</td>
            <td>${entity.description }</td>
            <td>${entity.location }</td>
            <td>${entity.type }</td>
            <td>${entity.start_time }</td>
            <td>
                <a href="UserAction.do?method=editUI">编辑</a>                
            </td>
        </tr>	
        </c:forEach>
  
        
</table>
</body>
</html>
<script>
    $(function () {
        

		$('#addnew').click(function(){

				window.location.href="UserAction.do?method=addUI";
		 });


    });

	function del(id)
	{
		
		
		if(confirm("确定要删除吗？"))
		{
		
			var url = "index.html";
			
			window.location.href=url;		
		
		}
	
	
	
	
	}
</script>