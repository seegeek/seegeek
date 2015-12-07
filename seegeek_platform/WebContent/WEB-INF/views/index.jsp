<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
 <head>
 <%@ include file="/WEB-INF/views/Public/commons.jspf" %>
  <title>后台管理系统</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="mobile-web-app-capable" content="yes">
<link rel="icon" sizes="192x192" href="assets/i/app-icon72x72@2x.png">
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   <link href="${basePath }assets/css/dpl-min.css" rel="stylesheet" type="text/css" />
  <link href="${basePath }assets/css/bui-min.css" rel="stylesheet" type="text/css" />
   <link href="${basePath }assets/css/main-min.css" rel="stylesheet" type="text/css" />
 </head>
 <body>

  <div class="header">
    
      <div class="dl-title">
       <!--<img src="/chinapost/Public/assets/img/top.png">-->
      </div>
    <div class="dl-log">欢迎您，<span class="dl-log-user" id="btnShow"> ${opUser.username}</span><a href="${basePath}op/SysUserAction.do?method=logout" title="退出系统" class="dl-log-quit">[退出]</a>
    </div>
  </div>
   <div class="content">
    <div class="dl-main-nav">
      <div class="dl-inform"><div class="dl-inform-title"><s class="dl-inform-icon dl-up"></s></div></div>
      <ul id="J_Nav"  class="nav-list ks-clear">
      </ul>
    </div>
    <ul id="J_NavContent" class="dl-tab-conten">

    </ul>
   </div>
  <script type="text/javascript" src="${basePath }assets/js/jquery-1.8.1.min.js"></script>
  <script type="text/javascript" src="${basePath }assets/js/bui-min.js"></script>
  <script type="text/javascript" src="${basePath }assets/js/common/main-min.js"></script>
  <script type="text/javascript" src="${basePath }assets/js/config-min.js"></script>
  <script>

    BUI.use('common/main',function(){
     $.ajax({  
                url: 'ResouceAction.do?method=getMenu',  
                type: 'GET',  
			success : function(data, textStatus, jqXHR) {
			//	alert("异步方法提交"+data);
				var config=data;
				for(var i=0;i<config.length;i++)
				{
						var module_array=config[i].menu;
						for(var j=0;j<module_array.length;j++)
						{
						$("#J_Nav").append("<li class='nav-item dl-selected'><div class='nav-item-inner nav-home'>"+module_array[j].text+"</div></li>");
						}
				}
				//parse json 
				 // var config = [{id:'1',menu:[{text:'系统管理',items:[{id:'3',text:'角色管理',href:'Role/index.html'},{id:'4',text:'用户管理',href:'UserAction.do?method=list'},{id:'6',text:'菜单管理',href:'Menu/index.html'}]}]},{id:'7',homePage : '9',menu:[{text:'业务管理',items:[{id:'12',text:'直播管理',href:'LiveMediaAction.do?method=list'},{id:'9',text:'查询业务',href:'Node/index.html'}]}]}];
				      new PageUtil.MainPage({
				        modulesConfig : config
				      });
				
			},
			dataType : "json"
                });  
    
 
    });
    
     BUI.use('bui/overlay',function(Overlay){
     var dialog = new Overlay.Dialog({
       title:'个人信息修改',
       width:500,
       height:250,
   		mask:true,  //设置是否模态
       buttons:[],
       bodyContent:'<form id="J_Form" class="well"><label class="control-label">城市：</label><input type="text" name="passwd" id="passwd"></form>',
       success:function () {
         alert('确认');
         this.hide();
       }
     });
   dialog.hide();
   $('#btnShow').on('click',function () {
     dialog.show();
   });
 });
  </script>
  
 </body>
</html>
<script>
function editUser()
{
alert("111");
}

</script>