package com.seegeek.cms.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/RoleAction.do")
public class RoleAction extends BaseAction{
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
