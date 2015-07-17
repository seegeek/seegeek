package com.seegeek.cms.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.seegeek.cms.domain.User;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 10, 2015 2:56:16 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
@Controller
@RequestMapping("/UserAction.do")
public class UserAction extends BaseAction{
	@RequestMapping(params = "method=toMain")
	public String toMain(ModelMap map, HttpServletRequest request,	HttpServletResponse response) {
		User user = getLoginUserBySesson(request);
		List<User> userList = userService.getAll(Constance.GET_ALL);
		System.out.println(userList);
		request.setAttribute("userList", userList);
		System.out.println("1111111111");
		return "index";
	}

	
	@RequestMapping(params = "method=list")
	public String list(ModelMap map, HttpServletRequest request,	HttpServletResponse response) {
		User user = getLoginUserBySesson(request);
		List<User> userList = userService.getAll(Constance.GET_ALL);
		System.out.println(userList);
		request.setAttribute("userList", userList);
		 System.out.println("1111111111");
		 return "User/index";
	}
	@RequestMapping(params = "method=addUI")
	public String addUI(ModelMap map, HttpServletRequest request,	HttpServletResponse response) {
		List<User> userList = userService.getAll(Constance.GET_ALL);
		System.out.println(userList);
		request.setAttribute("userList", userList);
		return "User/add";
	}
	
	@RequestMapping(params = "method=add")
	public String add(ModelMap map, HttpServletRequest request,	HttpServletResponse response) {
		User userForm=new User();
		userForm.setEmail(request.getParameter("email"));
		userForm.setLoginName(request.getParameter("loginName"));
		userService.add(Constance.ADD_OBJECT, userForm);
		return "redirect:/UserAction.do?method=list";
	}
	
	@RequestMapping(params = "method=edit")
	public String edit(ModelMap map, HttpServletRequest request,	HttpServletResponse response) {
		User userForm=new User();
		userForm.setId(Integer.valueOf(request.getParameter("id")));
		userForm.setEmail(request.getParameter("email"));
		userForm.setPasswd(request.getParameter("passwd"));
		userForm.setNickname(request.getParameter("nickname"));
		userForm.setMobilePhone(request.getParameter("mobilePhone"));
		userForm.setIMEI(request.getParameter("IMEI"));
		userService.update(Constance.UPDATE_OBJECT, userForm);
		return "redirect:/UserAction.do?method=list";
	}
	@RequestMapping(params = "method=editUI")
	public String editUI(ModelMap map, HttpServletRequest request,	HttpServletResponse response) {
		Integer id=Integer.valueOf(request.getParameter("id"));
		User user=userService.get(Constance.GET_ONE, id);
		request.setAttribute("user", user);
		 System.out.println("1111111111");
		 return "User/edit";
	}
	
	

	 public ModelAndView getAll() {  
		 System.out.println("2222222");
		 return null;
	    }  
	 @RequestMapping(params = "method=getMenu")
		public String getMenu(ModelMap map, HttpServletRequest request,	HttpServletResponse response)
		{
			User user = getLoginUserBySesson(request);
			List<User> userList = userService.getAll(Constance.GET_ALL);
			System.out.println(userList);
			Map a=new HashMap();
			a.put("json", userList);
			JSONObject jo = JSONObject.fromObject(a);
			try {
				response.getWriter().print(jo);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	 

}
