package com.seegeek.cms.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.seegeek.cms.domain.Department;
import com.seegeek.cms.utils.PageBean;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 10, 2015 2:56:16 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
@Controller
@RequestMapping("/op/DepartmentAction.do")
public class DepartmentAction extends BaseAction{

	@RequestMapping(params = "method=list")
	public String list(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		String name=request.getParameter("name");
		request.setAttribute("name", name);
		return "Department/index";
	}
	
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
		PageBean<Department> pageBean= departmentService.queryPage(Constance.GET_ALL, hashmap);
		List<Department> entityList=pageBean.getResultList();
		JSONObject resp=new JSONObject();
		JSONArray array = new JSONArray();
		for(Department department:entityList)
		{
			JSONObject object=new JSONObject();
			object.put("id", department.getId());
			object.put("name", department.getName());
			object.put("description", department.getDescription());
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
	 
	@RequestMapping(params = "method=addUI")
	public String addUI(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
//		List<User> userList = userService.getAll(Constance.GET_ALL);
//		System.out.println(userList);
//		request.setAttribute("userList", userList);
		List<Department> deList=departmentService.getAll(Constance.GET_ALL);
		request.setAttribute("deList", deList);
		return "Department/add";
	}
	

	@RequestMapping(params = "method=add")
	public String add(@ModelAttribute("form") Department form, HttpServletRequest request,
			HttpServletResponse response) {
		departmentService.add(Constance.ADD_OBJECT, form);
	
		return "redirect:/op/DepartmentAction.do?method=list";
	}
	
	
	@RequestMapping(params = "method=edit",method = RequestMethod.POST)
	public String edit(@ModelAttribute("form") Department form) {
		departmentService.update(Constance.UPDATE_OBJECT, form);
		return "redirect:/op/DepartmentAction.do?method=list";
	}

	@RequestMapping(params = "method=editUI")
	public String editUI(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		Department department = departmentService.get(Constance.GET_ONE, id);
		request.setAttribute("department", department);
		List<Department> deList=departmentService.getAll(Constance.GET_ALL);
		request.setAttribute("deList", deList);
		return "Department/edit";
	}
	@RequestMapping(params = "method=delete")
	public String delete(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		departmentService.delete(Constance.DELETE_OBJECT, id);
		return "redirect:/op/DepartmentAction.do?method=list";
	}
}
