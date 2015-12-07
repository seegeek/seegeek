package com.seegeek.cms.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.seegeek.cms.domain.Resource;
import com.seegeek.cms.domain.Role;
import com.seegeek.cms.domain.RoleResource;
import com.seegeek.cms.utils.NumberTools;
import com.seegeek.cms.utils.PageBean;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 10, 2015 2:56:16 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
@Controller
@RequestMapping("/op/RoleAction.do")
public class RoleAction extends BaseAction {
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=list_json")
	public String list_json(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json; charset=utf-8");
		//			User user = getLoginUserBySesson(request);
		Map<String, Object> hashmap=new HashMap<String, Object>();
		/**
		 *   2. limit : 单页多少条记录
         *  3. pageIndex : 第几页，同start参数重复，可以选择其中一个使用
		 */
		hashmap.put("name", request.getParameter("name"));
		hashmap.put("startRow", request.getParameter("start")==null?0:request.getParameter("start"));
		hashmap.put("pageNum", request.getParameter("pageIndex").equals("0")?1:request.getParameter("pageIndex"));
		hashmap.put("pageSize", request.getParameter("limit").equals("0")?1:request.getParameter("limit"));
		PageBean<Role> pageBean= roleService.queryPage(Constance.GET_ALL, hashmap);
		List<Role> entityList=pageBean.getResultList();
		JSONObject resp=new JSONObject();
		JSONArray array = new JSONArray();
		for(Role role:entityList)
		{
			JSONObject object=new JSONObject();
			object.put("id", role.getId());
			object.put("name", role.getName());
			object.put("description", role.getDescription());
			array.add(object);
		}
		resp.put("rows", array.toString());
		resp.put("results", pageBean.getTotalRows());
		resp.put("hasError", false);
		resp.put("error", "");
		try {
			PrintWriter out = response.getWriter();
			out.print(resp);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(params = "method=list")
	public String list(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		String name=request.getParameter("name");
		request.setAttribute("name", name);
		return "Role/index";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=getMenu")
	public String getMenu(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json; charset=utf-8");
		List<Resource> resouceList = resourceService.getAll(Constance.GET_PARENTS_NULL);
		JSONArray array = new JSONArray();
		for(Resource resource:resouceList)
		{
			JSONObject object=new JSONObject();
			object.put("id", resource.getId());
			object.put("text", resource.getName());
			array.add(object);
		}
		System.out.println(resouceList);
		try {
			PrintWriter out = response.getWriter();
			out.print(array);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping(params = "method=addUI")
	public String addUI(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
//		List<User> userList = userService.getAll(Constance.GET_ALL);
//		System.out.println(userList);
//		request.setAttribute("userList", userList);
		
		return "Role/add";
	}
	
	@RequestMapping(params = "method=add")
	public String add(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Role form = new Role();
		form.setName(request.getParameter("name"));
		form.setDescription(request.getParameter("description"));
		roleService.add(Constance.ADD_OBJECT, form);
		return "redirect:/op/RoleAction.do?method=list";
	}
	@RequestMapping(params = "method=delete")
	public String delete(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		roleService.delete(Constance.DELETE_OBJECT, id);
		return "redirect:/op/RoleAction.do?method=list";
	}
	
	@RequestMapping(params = "method=edit")
	public String edit(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Role form = new Role();
		form.setId(Integer.valueOf(request.getParameter("id")));
		form.setName(request.getParameter("name"));
		form.setDescription(request.getParameter("description"));
		roleService.update(Constance.UPDATE_OBJECT, form);
		return "redirect:/op/RoleAction.do?method=list";
	}

	@RequestMapping(params = "method=editUI")
	public String editUI(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		Role role = roleService.get(Constance.GET_ONE, id);
		request.setAttribute("role", role);
		return "Role/edit";
	}
	/** 设置权限页面 */
	@RequestMapping(params = "method=setPrivilegeJSON")
	public String setPrivilegeJSON(ModelMap map, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		response.setContentType("text/json; charset=utf-8");
		//安装roleId 进行查询
		String roleId=request.getParameter("roleId");
		RoleResource tempR=new RoleResource();
		tempR.setRoleId(Integer.valueOf(roleId));
		List<RoleResource> resouceListByRoleId = roleResourceService.getList(Constance.GET_ALL,tempR);
	    List<Integer> resourceIdList=new ArrayList<Integer>();
		for(RoleResource roleR:resouceListByRoleId)
		{
			resourceIdList.add(roleR.getResourceId());
		}
		
		logger.info(resouceListByRoleId.size());
		//查询所有top节点
		List<Resource> resouceList = resourceService.getAll(Constance.GET_PARENTS_NULL);
		JSONArray arrayL1 = new JSONArray();
		JSONObject jbL1=null;
		for(Resource r:resouceList)
		{
			//一级菜单
			jbL1=new JSONObject();
			jbL1.put("id", r.getId());
			jbL1.put("text", r.getName());
			jbL1.put("expanded", false);
			if(resourceIdList!=null&&resourceIdList.size()>0)
			{
				System.out.println("...."+r.getId()+"----------"+resourceIdList.contains(r.getId()));
				if(resourceIdList.contains(r.getId()))
				{
					jbL1.put("checked", true);
				}
				else
				{
					jbL1.put("checked", false);
				}
			}
			JSONArray arrayL2 = new JSONArray();
			for (Resource rr:r.getChildren())
			{
				JSONObject	jbL2=new JSONObject();
				jbL2.put("id", rr.getId());
				jbL2.put("text", rr.getName());
				if(resourceIdList.contains(rr.getId()))
				{
					jbL2.put("checked", true);
				}
				else
				{
					jbL2.put("checked", false);
				}
				arrayL2.add(jbL2);
			}
			jbL1.put("children", arrayL2);
			arrayL1.add(jbL1);
		}
		try {
			PrintWriter out = response.getWriter();
			out.print(arrayL1.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping(params = "method=setPrivilegeUI")
	public String setPrivilegeUI(ModelMap map, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return "Role/setPrivilegeUI";
	}

	/** 设置权限 */

	@RequestMapping(params = "method=setPrivilege")
	public String setPrivilege(ModelMap map, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String resourceIds=request.getParameter("resourceIds");
		String resourceParentIds=request.getParameter("resourceParentIds");
		String roleId=request.getParameter("roleId");
		List<String> ids_list=new ArrayList<String>();
		String[] rids=resourceIds.split(",");
		String[] rParentids=resourceParentIds.split(",");
		ids_list.addAll(NumberTools.arrayconverToListString(rids));
		ids_list.addAll(NumberTools.arrayconverToListString(rParentids));
		List<RoleResource> list=new ArrayList<RoleResource>();
		RoleResource roleResource=null;
		for(int i=0;i<ids_list.size();i++)
		{
			roleResource=new RoleResource();
			roleResource.setResourceId(Integer.valueOf(ids_list.get(i)));
			roleResource.setRoleId(Integer.valueOf(Integer.valueOf(roleId)));
			list.add(roleResource);
		}
		roleResourceService.add(Constance.ADD_ROLE_RESOUCES, list);
		return "Role/index";
	}


}
