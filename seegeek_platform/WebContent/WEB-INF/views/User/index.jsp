<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
 <head>
 <%@ include file="/WEB-INF/views/Public/commons.jspf" %>
    <title>用户管理</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
    content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<meta name="mobile-web-app-capable" content="yes">
<link rel="icon" sizes="192x192" href="assets/i/app-icon72x72@2x.png">
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
<form class="form-inline definewidth m20" action="UserAction.do?method=list" id="myform" method="post">    
    用户名称：
        <input type="text" name="name" value="${name}" id="name"class="abc input-default" placeholder="" value="">&nbsp;&nbsp;  
    <button type="button" id="search" class="btn btn-primary">查询</button>&nbsp;&nbsp; <button type="button" class="btn btn-success" id="addnew">新增用户</button>
</form>
<br/>
<div class="demo-content">
    <div class="row">
        <div id="grid" style="margin-left: 10px;">
          
        </div>
    </div>
    
 
  <script src="http://g.tbcdn.cn/fi/bui/jquery-1.8.1.min.js"></script>
  <script src="http://g.alicdn.com/bui/seajs/2.3.0/sea.js"></script>
  <script src="http://g.alicdn.com/bui/bui/1.1.21/config.js"></script>
    <script type="text/javascript">
        BUI.use(['bui/grid','bui/data'],function(Grid,Data){
            var Grid = Grid,
          Store = Data.Store,
          columns = [
            {title : 'ID',dataIndex :'id', width:100},
            {title : '帐号',dataIndex :'loginName', width:100},
            {id: '123',title : '昵称',dataIndex :'nickname', width:100},
            {title : '手机号',dataIndex : 'mobilePhone',width:100},
            {title : 'home_address',dataIndex : 'personal_signature',width:100},
            {title : 'work_address',dataIndex : 'work_address',width:100},
            {title : 'email',dataIndex : 'email',width:100},
            {title : 'IMEI',dataIndex : 'IMEI',width:100},
            {title : 'location_x',dataIndex : 'location_x',width:100},
            {title : 'location_y',dataIndex : 'location_y',width:100},
            {title : '操作',width : 100,dataIndex:'e',renderer : function(value,obj){
              return '<a  href="UserAction.do?method=editUI&id='+obj.id +'" class="grid-command btn-edit"><i class="icon icon-edit"></i> 修改</a><a  href="UserAction.do?method=delete&id='+obj.id +'" class="grid-command btn-del" onClick="del()"><i class="icon icon-trash"></i> 删除</a><a  href="UserAction.do?method=lockUI&id='+obj.id +'" class="grid-command btn-lock"><i class="icon icon-lock"></i> 封号</a>'
            }
            }
          ];
 
        /**
         * 自动发送的数据格式：
         *  1. start: 开始记录的起始数，如第 20 条,从0开始
         *  2. limit : 单页多少条记录
         *  3. pageIndex : 第几页，同start参数重复，可以选择其中一个使用
         *
         * 返回的数据格式：
         *  {
         *     "rows" : [{},{}], //数据集合
         *     "results" : 100, //记录总数
         *     "hasError" : false, //是否存在错误
         *     "error" : "" // 仅在 hasError : true 时使用
         *   }
         * 
         */
        var store = new Store({
            url : 'UserAction.do?method=list_json',
            autoLoad:true, //自动加载数据
            params : { //配置初始请求的参数
               name :$("#username").val(),
              b : 'b1'
            },
            pageSize:10	// 配置分页数目
          }),
          grid = new Grid.Grid({
            render:'#grid',
            columns : columns,
            loadMask: true, //加载数据时显示屏蔽层
            store: store,
            // 底部工具栏
            bbar:{
                // pagingBar:表明包含分页栏
                pagingBar:true
            }
          });
 
        grid.render();
      });
    </script>
  </div>
</body>
</html>
<script>
    $(function () {
        

		$('#addnew').click(function(){
				window.location.href="UserAction.do?method=addUI";
		 });
		$('#search').click(function(){
			("#myform").submit();
		 });

    });

	function del(id)
	{
		if(!confirm("确定要删除吗？"))
		{
		window.event.returnValue = false;		
		}
	}
</script>