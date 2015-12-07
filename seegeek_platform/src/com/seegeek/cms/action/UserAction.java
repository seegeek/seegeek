package com.seegeek.cms.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import Decoder.BASE64Encoder;

import com.seegeek.cms.domain.User;
import com.seegeek.cms.utils.PageBean;


@Controller
@RequestMapping("/op/UserAction.do")
public class UserAction extends BaseAction {
	private static final int EXPIRES = 3;

	@RequestMapping(params = "method=toMain")
	public String toMain(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
//				List<User> userList=userService.getAll(Constance.GET_ALL);
//				request.setAttribute("userList", userList);
				return "index";
	}

//	@RequestMapping(params = "method=login")
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public @ResponseBody int login(HttpServletRequest request, @RequestParam("randCode") String randCode, @RequestParam("username") String username,@RequestParam("passwd") String passwd,
			HttpServletResponse response) {
		User entity = new User();
		entity.setNickname(username);
		entity.setPasswd(passwd);
		String serverCode = (String) request.getSession().getAttribute("rand");
		// 1.服务器端验证码有误 或 客户端验证码有误 或 两者都不相等
		if (!StringUtils.isNotEmpty(serverCode) || !StringUtils.isNotEmpty(randCode)
				) {
			request.setAttribute("error", "请输入正确的验证码");
	        return -1;
		}

		User user = null;
		System.out.println(randCode+">>>>>>"+serverCode);
		user =userService.get(Constance.CHECKLOGIN, entity);
		request.getSession().setAttribute("user", user);
		if (user != null&&serverCode.equals(randCode.trim())) {
			return 0;
		}
		return -1;
	}

	public String md5Value(String value) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte result[] = digest.digest(value.getBytes());
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(result);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	};

	@RequestMapping(params = "method=list")
	public String list(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
//		User user = getLoginUserBySesson(request);
		List<User> userList = userService.getAll(Constance.GET_ALL);
		System.out.println(userList);
		request.setAttribute("userList", userList);
//		System.out.println("1111111111");
		String name=request.getParameter("name");
		request.setAttribute("name", name);
		return "User/index";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=list_json")
	public String list_json(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json; charset=utf-8");
		//			User user = getLoginUserBySesson(request);
		Map<String, Object> hashmap=new HashMap<String, Object>();
		System.out.println(request.getParameter("start"));
		System.out.println(request.getParameter("limit"));
		System.out.println(request.getParameter("pageIndex"));
		/**
		 *   2. limit : 
		 */
		hashmap.put("name", request.getParameter("name"));
		hashmap.put("startRow", request.getParameter("start")==null?0:request.getParameter("start"));
		hashmap.put("pageNum", request.getParameter("pageIndex").equals("0")?1:request.getParameter("pageIndex"));
		hashmap.put("pageSize", request.getParameter("limit").equals("0")?1:request.getParameter("limit"));
		PageBean<User> pageBean= userService.queryPage(Constance.GET_ALL, hashmap);
		List<User> userlist=pageBean.getResultList();
		JSONObject resp=new JSONObject();
		JSONArray array = new JSONArray();
		for(User user:userlist)
		{
			JSONObject object=new JSONObject();
			object.put("id", user.getId());
			object.put("loginName", user.getLoginName());
			object.put("nickname", user.getNickname());
			object.put("mobilePhone", user.getMobilePhone());
			object.put("personal_signature", user.getPersonal_signature());
			object.put("home_address", user.getHome_address());
			object.put("work_address", user.getWork_address());
			object.put("email", user.getEmail());
			object.put("IMEI", user.getIMEI());
			object.put("location_x", user.getLocation_x());
			object.put("location_y", user.getLocation_y());
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
		return "User/add";
	}

	@RequestMapping(params = "method=add")
	public String add(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		User userForm = new User();
		userForm.setEmail(request.getParameter("email"));
		userForm.setLoginName(request.getParameter("loginName"));
		userService.add(Constance.ADD_OBJECT, userForm);
		return "redirect:/UserAction.do?method=list";
	}

	@RequestMapping(params = "method=lock")
	public String lock(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		User userForm = new User();
		userForm.setId(Integer.valueOf(request.getParameter("id")));
		userForm.setEmail(request.getParameter("email"));
		userForm.setPasswd(request.getParameter("passwd"));
		userForm.setNickname(request.getParameter("nickname"));
		userForm.setMobilePhone(request.getParameter("mobilePhone"));
		userForm.setIMEI(request.getParameter("IMEI"));
		userService.update(Constance.UPDATE_OBJECT, userForm);
//		return "redirect:/UserAction.do?method=list";
		return "User/index";
	}
	@RequestMapping(params = "method=edit")
	public String edit(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		User userForm = new User();
		userForm.setId(Integer.valueOf(request.getParameter("id")));
		userForm.setEmail(request.getParameter("email"));
		userForm.setPasswd(request.getParameter("passwd"));
		userForm.setNickname(request.getParameter("nickname"));
		userForm.setMobilePhone(request.getParameter("mobilePhone"));
		userForm.setIMEI(request.getParameter("IMEI"));
		userService.update(Constance.UPDATE_OBJECT, userForm);
//		return "redirect:/UserAction.do?method=list";
		return "User/index";
	}

	@RequestMapping(params = "method=editUI")
	public String editUI(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		User user = userService.get(Constance.GET_ONE, id);
		request.setAttribute("user", user);
		System.out.println("1111111111");
		return "User/edit";
	}
	@RequestMapping(params = "method=lockUI")
	public String lockUI(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		User user = userService.get(Constance.GET_ONE, id);
		request.setAttribute("user", user);
		System.out.println("1111111111");
		return "User/lockUI";
	}

	public ModelAndView getAll() {
		System.out.println("2222222");
		return null;
	}

	@RequestMapping(params = "method=getMenu")
	public String getMenu(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		User user = getLoginUserBySesson(request);
		List<User> userList = userService.getAll(Constance.GET_ALL);
		System.out.println(userList);
		Map a = new HashMap();
		a.put("json", userList);
		JSONObject jo = JSONObject.fromObject(a);
		try {
			response.getWriter().print(jo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping(params = "method=delete")
	public String delete(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		userService.delete(Constance.DELETE_OBJECT, id);
		//删除该用户所关联的所有数据
//		return "redirect:/op/CommentAction.do?method=list";
		return "User/index";
	}
}
