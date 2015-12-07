<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
 <head>
 <%@ include file="/WEB-INF/views/Public/commons.jspf" %>
    <title>直播管理</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${basePath }Css/bootstrap.css" />
    <link rel="stylesheet" type="text/css" href="${basePath }/Css/bootstrap-responsive.css" />
    <link rel="stylesheet" type="text/css" href="${basePath }/Css/style.css" />
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/default/dpl.css" rel="stylesheet">
  <link href="http://g.alicdn.com/bui/bui/1.1.21/css/default/bui.css" rel="stylesheet">
 
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
 <div class="demo-content">
<link rel="stylesheet" href="http://g.tbcdn.cn/fi/bui/css/layout-min.css">
<style>
  .bui-pagingbar-number{
    margin-right: 220px;
  }
</style>
<style type="text/css">
  .x-layout-item-flow{
    float: left;
    overflow: hidden;
    padding: 10px;
  }
</style>
<input id="play_type" name="play_type" type="hidden" value="${play_type}"/>
    <div class="detail-section">  
      <div class="row">
        <div id="J_Layout" class="span16">
        </div>
      </div>
        <div id="bar"></div>
     
    </div>
    
    
 
  <script src="http://g.tbcdn.cn/fi/bui/jquery-1.8.1.min.js"></script>
  <script src="http://g.alicdn.com/bui/seajs/2.3.0/sea.js"></script>
  <script src="http://g.alicdn.com/bui/bui/1.1.21/config.js"></script>
 
 
 
   
 
<!-- script start --> 
  <script type="text/javascript">
  
   
  		var config;
       $.ajax({  
                url: 'LiveMediaAction.do?method=list_json',  
                type: 'GET',  
                data:{limit:10,pageIndex:1,play_type:$("#play_type").val()},
                async: false,
				success : function(data, textStatus, jqXHR) {
			//	alert("异步方法提交"+data);
				 config=data;
			},
			dataType : "json"
                });  
 
  
  
  
  
        BUI.use(['bui/layout','bui/toolbar'],function (Layout,Toolbar) {
        var layout = new Layout.Flow({
          columns : 4
        }),
        control = new BUI.Component.Controller({
          width:880,
          render : '#J_Layout',
          elCls : 'layout-test',
          defaultChildCfg : {
            xclass : 'controller',
            tpl : '<iframe height=300 width=200 name="I1" src="http://player.youku.com/embed/XNTIzMTcyMzA4">aa</iframe>',
            height : 300,
            width : 200
          },
          children : config.rows,
          plugins : [layout]
      });
 	 var bar = new Toolbar.NumberPagingBar({
          render : '#bar',
          elCls : 'pagination pull-right',
        });
        bar.render();
      control.render();
    });
  </script>
<!-- script end -->
  </div>
  </body>
</html>
<script>
function test(id)
{
alert(id);
}
</script>