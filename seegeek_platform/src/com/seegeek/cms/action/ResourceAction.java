package com.seegeek.cms.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.seegeek.cms.domain.OPUser;
import com.seegeek.cms.domain.Resource;
import com.seegeek.cms.domain.Role;
import com.seegeek.cms.domain.User;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 10, 2015 2:56:16 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
@Controller
@RequestMapping("/op/ResouceAction.do")
public class ResourceAction extends BaseAction{
	@RequestMapping(params = "method=toGetAllUser")
	public String toGetAllUser(ModelMap map, HttpServletRequest request,	HttpServletResponse response) {
		User user = getLoginUserBySesson(request);
		List<User> userList = userService.getAll(Constance.GET_ALL);
		System.out.println(userList);
		request.setAttribute("userList", userList);
		 System.out.println("1111111111");
		 return "index";
	}

	 public ModelAndView getAll() {  
		 System.out.println("2222222");
		 return null;
	    }  
	 @SuppressWarnings("unchecked")
 	@RequestMapping(params = "method=getMenu")
		public String getMenu(ModelMap map, HttpServletRequest request,	HttpServletResponse response)
		{
		 
			response.setContentType("text/json; charset=utf-8");
			User user=getLoginUserBySesson(request);
			//当前用户所拥有的权限
			List<Resource> selectedList=new ArrayList<Resource>();
			List<Resource> resouceList = resourceService.getAll(Constance.GET_PARENTS_NULL);
			if(!user.getLoginName().equals("admin"))
			{
				User newInfo=userService.get("selectUserRole",user.getId());
				List<String> roleIds=new ArrayList<String>();
				for(Role role:newInfo.getRoleList())
				{
					roleIds.add(role.getId().toString());
				}
				selectedList = resourceService.getList("getListByIds",roleIds);
			}
			else
			{
				//##  admin
				selectedList=resourceService.getAll(Constance.GET_ALL);
			}
		//#####################将用户所有的权限拿出来
		
		//#########
			
			JSONArray array = new JSONArray();
			for(Resource resource:resouceList)
			{
				if(selectedList.contains(resource))
				{
				
				JSONObject object=new JSONObject();
				object.put("id", resource.getId());
				//menu 	
				JSONArray menuArray=new JSONArray();
				//for child
				
				JSONObject object2=new JSONObject();
				object2.put("id", resource.getId());
				object2.put("text", resource.getName());
//				object2.put("collapsed", true);
			
				
				    JSONArray itemsArray=new JSONArray();
					for(Resource child:resource.getChildren())
					{
					
						if(selectedList.contains(child))
						{
							JSONObject object3=new JSONObject();
							object3.put("id", child.getId());
							object3.put("text", child.getName());
							object3.put("href", child.getUrl());
							itemsArray.add(object3);
						}
					}
				//items 添加
				object2.put("items", itemsArray);
				
				
				menuArray.add(object2);
				
				
				object.put("menu", menuArray);
				array.add(object);
				}
			}
			try {
				PrintWriter out = response.getWriter();
				out.print(array);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	 
	 
}
