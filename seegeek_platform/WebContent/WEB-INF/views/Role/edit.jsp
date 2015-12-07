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
<form action="RoleAction.do?method=edit" method="post" class="definewidth m20">
<input type="hidden" name="id" value="${role.id}" />
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td width="10%" class="tableleft">名称</td>
            <td><input type="text" name="name" value="${role.name}"/></td>
        </tr>
        <tr>
            <td class="tableleft">描述</td>
            <td><input type="text" name="description" value="${role.description}"/></td>
        </tr>
       
        <!--  
        <tr>
            <td class="tableleft">状态</td>
            <td>
                <input type="radio" name="status" value="0"
                    <eq name="user.status" value='0'>checked</eq> /> 启用
              <input type="radio" name="status" value="1"
                    <eq name="user.status" value='1'>checked</eq> /> 禁用
            </td>
        </tr>
        <tr>
            <td class="tableleft">角色</td>
            <td>{$role_checkbox}</td>
        </tr>-->
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