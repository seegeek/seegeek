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
<form action="DepartmentAction.do?method=edit" method="post" class="definewidth m20">
<input type="hidden" name="id" value="${department.id}" />
    <table class="table table-bordered table-hover definewidth m10">
       <tr>
        <td class="tableleft">菜单</td>
        <td> <select name="departmentId">
		<c:forEach var="r" items="${deList}">
		<option value="${r.id }">${r.name }</option>
		</c:forEach>
        </select>
        </td>
  	  </tr>
        <tr>
            <td width="10%" class="tableleft">名称</td>
            <td><input type="text" name="name" value="${department.name}"/></td>
        </tr>
        <tr>
            <td class="tableleft">描述</td>
            <td><input type="text" name="description" value="${department.description}"/></td>
        </tr>
        <tr>
            <td class="tableleft"></td>
            <td>
                <button type="submit" class="btn btn-primary" type="button">保存</button>				 &nbsp;&nbsp;<button type="button" class="btn btn-success" name="backid" id="backid">返回列表</button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
<script>
    $(function () {       
		$('#backid').click(function(){
				window.location.href="{:U('User/index')}";
		 });

    });
</script>