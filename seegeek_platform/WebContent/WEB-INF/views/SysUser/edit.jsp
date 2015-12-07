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
<form action="SysUserAction.do?method=edit" method="post" class="definewidth m20">
<input type="hidden" name="id" value="${param.id}" />
    <table class="table table-bordered table-hover definewidth m10">
      <tr>
        <td class="tableleft">组织</td>
        <td>
        <select name="departmentId">
			<option value=""> 请选择</option>
		<c:forEach var="r" items="${departmentList}">
		  <option value="${r.id }" ${r.id eq user.departmentId ?'selected':''}>${r.name}</option>
		</c:forEach>
        </select>
        </td>
    </tr>
        <tr>
            <td width="10%" class="tableleft">用户名</td>
            <td><input type="text" name="loginName" value="${user.loginName}"/></td>
        </tr>
        <tr>
            <td class="tableleft">密码</td>
            <td><input type="passwd" name="passwd" value="${user.passwd}"/></td>
        </tr>
        <tr>
            <td class="tableleft">邮箱</td>
            <td><input type="text" name="email" value="${user.email}"/></td>
        </tr>
        <tr>
            <td class="tableleft">电话</td>
            <td><input type="text" name="mobilePhone" value="${user.mobilePhone}"/></td>
        </tr>
          <tr>
        <td class="tableleft">性别</td>
        <td>
      
        <c:if test="${user.sex eq 0}">
         <input type="radio" name="sex" value="0" checked="checked"/> 男
          <input type="radio" name="sex" value="1"/> 女
        </c:if>
        <c:if test="${user.sex eq 1}">
         <input type="radio" name="sex" value="0"/> 男
         <input type="radio" name="sex" value="1" checked="checked"/> 女
        </c:if>
        </td>
   	 </tr>
       <tr>
        <td class="tableleft">角色</td>
        <td>
        <select name="roleIds" multiple="multiple">
		<c:forEach var="r" items="${roleList}">
	
		<c:choose>
		<c:when test="${r.checked eq true}">
		<option value="${r.id }" selected="selected">${r.name }</option>
		</c:when>
		<c:when test="${r.checked eq false}">
		<option value="${r.id }">${r.name }</option>
		</c:when>
		</c:choose>
	
		</c:forEach>
        </select>
        </td>
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
				window.location.href="{:U('SysUser/index')}";
		 });

    });
</script>