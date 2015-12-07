<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
 <head>
 <%@ include file="/WEB-INF/views/Public/commons.jspf" %>
    <title>直播管理</title>
    <meta charset="UTF-8">
          <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
        content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="mobile-web-app-capable" content="yes">
<link rel="icon" sizes="192x192" href="assets/i/app-icon72x72@2x.png">
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
<form class="form-inline definewidth m20" id="myform" action="LiveMediaAction.do?method=vod_list" method="post">    
<input id="time_val" type="hidden" value="${time}"/>
<input id="play_type" name="play_type" type="hidden" value="${play_type}"/>
 <!--  直播名称：
  <input type="text" name="username" value="${name}" id="username"class="abc input-default" placeholder="" value="">&nbsp;&nbsp;  -->   
  筛选:&nbsp;&nbsp;<select name="time" id="time">
  	<option value="">时间</option>
  	<option value="1">最近10分钟</option>
  	<option value="2">最近1小时</option>
  	<option value="3">今天</option>
  	<option value="4">昨天</option>
  	<option value="5">本月</option>
  	</select> 
  	<select name="location" id="location">
  	<option value="" selected="selected">城市</option>
  	<option value="北京市"  ${location eq '北京市'?'selected':''}>北京市</option>
  	<option value="上海市"  ${location eq '上海市'?'selected':''}>上海市</option>
  	</select> 
 	<input type="text" value="${title}" id="title" name="title" placeholder="标题"/>
  	<input type="text" value="${nickname}" id="nickname" name="nickname" placeholder="用户"/>
  
  
  
  <iamp-identify:IAMPIdentify authid="04_04_03_00">
    <button type="button" id="search" class="btn btn-primary">查询</button>&nbsp;&nbsp; <button type="button" class="btn btn-success" id="addnew">新增用户</button>
	</iamp-identify:IAMPIdentify>
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
            {title : 'ID',dataIndex :'id', width:50},
            {title : '点播标题',dataIndex :'title', width:100},
            {title : '点播标签',dataIndex :'tag', width:100},
            {title : '点播描述',dataIndex : 'description',width:100},
            {title : '地理位置',dataIndex : 'location',width:100},
            {title : '类型',dataIndex : 'type',width:100},
            {title : 'recordingId',dataIndex : 'recordingId',width:150},
            {title : '图片',dataIndex : 'frame',width:300},
            {title : '操作',width : 100,dataIndex:'e',renderer : function(value,obj){
              return '<a  href="LiveMediaAction.do?method=delete&id='+obj.id +'&play_type='+${play_type}+'" class="grid-command btn-del" onClick="del()"><i class="icon icon-trash"></i> 删除</a>'
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
            url : 'LiveMediaAction.do?method=list_json',
            autoLoad:true, //自动加载数据
            params : { //配置初始请求的参数
              time :$("#time").val(),
              title :$("#title").val(),
              play_type :$("#play_type").val(),
              location :$("#location").val(),
              nickname :$("#nickname").val(),
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
		$('#search').click(function(){
		$("#myform").submit();
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