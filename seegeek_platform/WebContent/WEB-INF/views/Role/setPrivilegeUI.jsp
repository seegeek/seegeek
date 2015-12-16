<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html>
 <head>
 <%@ include file="/WEB-INF/views/Public/commons.jspf" %>
 
    <title>角色管理</title>
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
<br/>
<br/>
<br/>
<div class="demo-content">
<form action="RoleAction.do?method=setPrivilege" method="post" id="myform">
<input type="text" name="resourceIds" id="resourceIds"/>
<input type="text" name="resourceParentIds" id="resourceParentIds"/>
<input type="text" name="rootIds" id="rootIds"/>
<input type="hidden" name="roleId" id="roleId" value="${param.id}"/>
    <div class="row">
      <div class="span8 offset3">
        <div id="t1">
          
        </div>
        <h2>点击的节点</h2>
        <div class="log well"></div>
       <button  class="btn btn-primary" type="button" onclick="return saveInfo()">保存</button>	
      </div>
  </div>
  </form> 
</div>
  <script type="text/javascript" src="${basePath }Js/jquery.js"></script>
  <script src="http://g.alicdn.com/bui/seajs/2.3.0/sea.js"></script>
  <script src="http://g.alicdn.com/bui/bui/1.1.21/config.js"></script>
 
<!-- script start --> 
    <script type="text/javascript">
        BUI.use('bui/tree',function (Tree) {
        
      //树节点数据，
      //text : 文本，
      //id : 节点的id,
      //leaf ：标示是否叶子节点，可以不提供，根据childern,是否为空判断
      //expanded ： 是否默认展开
      //checked : 节点是否默认选中
      
    //  var data = [ 
     //     {text : '1',id : '1',checked : true,children: [{text : '11',id : '11'}]},
     //     {text : '2',id : '2',expanded : true,children : [
    //          {text : '21',id : '21',children : [{text : '211',id : '211'},{text : '212',id : '212'}]},
    //          {text : '22',id : '22'}
     //     ]},
    //      {text : '3',id : '3'},
    //      {text : '4',id : '4'}
    //    ];
    var roleId=$("#roleId").val();
     $.ajax({  
                url: 'RoleAction.do?method=setPrivilegeJSON&roleId='+roleId,  
                type: 'GET',  
			success : function(data, textStatus, jqXHR) {
				 //由于这个树，不显示根节点，所以可以不指定根节点
      var tree = new Tree.TreeList({
        render : '#t1',
        nodes : data,
        checkType: 'all', //checkType:勾选模式，提供了4中，all,onlyLeaf,none,custom
        showLine : true //显示连接线
      });
	      	tree.render();
	      	var selected = tree.getCheckedLeaf();
	        var ids_str = '';
	        var parentId='';
	        var rootId='';
			 BUI.each(selected,function(node){
			 if(parentId.indexOf(node.parent.id)==-1)
	        	{
		          parentId += node.parent.id + ',';
	        	}
				if(node.parent.parent!=null&&rootId.indexOf(node.parent.parent.id)==-1)
	        	{
	        		rootId += node.parent.parent.id + ',';
	        	}
	          ids_str += node.id + ',';
	        });
	        $('.log').text(ids_str);
	        $('#resourceIds').val(ids_str);
	       $('#resourceParentIds').val(parentId);
	       $('#rootIds').val(rootId);
	 <!--tree 开始渲染-->
	      tree.on('checkedchange',function(ev){
	        var checkedNodes=tree.getCheckedLeaf();
	        var str = '';
	        var parentId='';
	        var rootId='';
	        BUI.each(checkedNodes,function(node){
	        	if(parentId.indexOf(node.parent.id)==-1)
	        	{
		          parentId += node.parent.id + ',';
	        	}
	        	if(node.parent.parent!=null&&rootId.indexOf(node.parent.parent.id)==-1)
	        	{
	        		rootId += node.parent.parent.id + ',';
	        	}
	        
	           str += node.id + ',';
	        });
	        $('.log').text(str);
	        $('#resourceIds').val(str);
	        $('#resourceParentIds').val(parentId);
	        $('#rootIds').val(rootId);
	      });
	     <!--tree已经结束-->
				
			},
			dataType : "json"
                });  
    
     
      
      
      
    });
    </script>
<!-- script end -->
</body>
</html>
<script>
function saveInfo()
{
var ids=$("#ids").val();
$("#myform").submit();
}
  $(function () {
        

		

    });


</script>