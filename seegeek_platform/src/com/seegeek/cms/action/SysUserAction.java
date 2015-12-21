package com.seegeek.cms.action;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import Decoder.BASE64Encoder;

import com.seegeek.cms.domain.Department;
import com.seegeek.cms.domain.Role;
import com.seegeek.cms.domain.User;
import com.seegeek.cms.domain.UserRole;
import com.seegeek.cms.redis.MessageStorage;
import com.seegeek.cms.utils.PageBean;

/**
 * @author 作者 zhaogaofei
 * @version 创建时间：May 10, 2015 2:56:16 PM
 * @email zhaogaofei2012@163.com 类说明 运营系统用户
 */
@Controller
@RequestMapping("/op/SysUserAction.do")
public class SysUserAction extends BaseAction {
	private static final int EXPIRES = 3;
	@Autowired
	public MessageStorage messageStorage;

	@RequestMapping(params = "method=toMain")
	public String toMain(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		// List<User> userList=userService.getAll(Constance.GET_ALL);
		// request.setAttribute("userList", userList);
		return "index";
	}

	@RequestMapping(params = "method=loginByUser")
	public String loginByUser(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("passwd");
		String randCode = request.getParameter("randCode");
		String serverCode = "";
		User entity = new User();
		entity.setLoginName(username);
		entity.setPasswd(password);
		entity = userService.get(Constance.CHECKLOGIN, entity);
		if (entity != null) {
			serverCode =(String) request.getSession().getAttribute("rand");
		} else {
			return "redirect:/login.jsp";
		}
		// 1.服务器端验证码有误 或 客户端验证码有误 或 两者都不相等
		if (!StringUtils.isNotEmpty(serverCode)
				|| !StringUtils.isNotEmpty(randCode)) {
			request.setAttribute("error", "验证码无效");
			return "redirect:/login.jsp";
		}

		if (!serverCode.equals(randCode.trim())) {
			request.setAttribute("error", "验证码无效");
			return "redirect:/login.jsp";
		}
		request.getSession().setAttribute("user", entity);

		return "index";

	}

	@RequestMapping(params = "method=loginByPhone")
	public String loginByPhone(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		String mobilePhone = request.getParameter("mobilePhone");
		String randCode = request.getParameter("randCode");
		String serverCode = "";
		User entity = new User();
		entity.setMobilePhone(mobilePhone);
		entity = userService.get(Constance.GET_BY_MOBILEPHONE, entity);
		if (entity != null) {
			serverCode = messageStorage.load(entity.getMobilePhone());
		} else {
			return "redirect:/login2.jsp";
		}
		// 1.服务器端验证码有误 或 客户端验证码有误 或 两者都不相等
		if (!StringUtils.isNotEmpty(serverCode)
				|| !StringUtils.isNotEmpty(randCode)) {
			request.setAttribute("error", "验证码无效");
			return "redirect:/login2.jsp";
		}

		if (!serverCode.equals(randCode.trim())) {
			request.setAttribute("error", "验证码无效");
			return "redirect:/login2.jsp";
		}
		request.getSession().setAttribute("user", entity);
		return "index";
	}

	public String md5Value(String value) {
		try {
			// 获取md5加密的对象
			MessageDigest digest = MessageDigest.getInstance("md5");
			// 将传入的数据装换为byte字节,在用digest进行转换(加密)成新的字节数组,
			byte result[] = digest.digest(value.getBytes());
			// 需要的jdk版本myeclipse2013中自带的com.sun.java.jdk.win32.x86_64_1.6.0.u43
			// jdk1.7.0_25没有相关的包
			BASE64Encoder encoder = new BASE64Encoder();
			// 返回加密后的数据
			return encoder.encode(result);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	};

	@RequestMapping(params = "method=logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request,
			HttpServletResponse response) {
		request.getSession().removeAttribute("opUser");
		// return "loginUI";
		return "redirect:/login.jsp";
	}

	@RequestMapping(params = "method=loginUI", method = RequestMethod.GET)
	public String loginUI(HttpServletRequest request,
			HttpServletResponse response) {
		return "login";
	}

	@RequestMapping(params = "method=list")
	public String list(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		String name = request.getParameter("name");
		request.setAttribute("name", name);
		return "SysUser/index";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=list_json")
	public String list_json(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json; charset=utf-8");
		Map<String, Object> hashmap = new HashMap<String, Object>();
		System.out.println(request.getParameter("start"));
		System.out.println(request.getParameter("limit"));
		System.out.println(request.getParameter("pageIndex"));
		/**
		 * 2. limit : 单页多少条记录 3. pageIndex : 第几页，同start参数重复，可以选择其中一个使用
		 */
		hashmap.put("name", request.getParameter("name"));
		hashmap.put("startRow", request.getParameter("start") == null ? 0
				: request.getParameter("start"));
		hashmap.put("pageNum",
				request.getParameter("pageIndex").equals("0") ? 1 : request
						.getParameter("pageIndex"));
		hashmap.put("pageSize", request.getParameter("limit").equals("0") ? 1
				: request.getParameter("limit"));
		PageBean<User> pageBean = userService.queryPage(Constance.GET_ALL,
				hashmap);
		List<User> userlist = pageBean.getResultList();
		JSONObject resp = new JSONObject();
		JSONArray array = new JSONArray();
		for (User user : userlist) {
			JSONObject object = new JSONObject();
			object.put("id", user.getId());
			object.put("nickName", user.getNickname());
			object.put("loginName", user.getLoginName());
			object.put("email", user.getEmail());
			object.put("phone", user.getMobilePhone());
			object.put("sex", user.getSex().equals("0") ? "男" : "女");
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
		// List<User> userList = userService.getAll(Constance.GET_ALL);
		// System.out.println(userList);
		List<Department> departmentList = departmentService
				.getAll(Constance.GET_ALL);
		List<Role> roleList = roleService.getAll(Constance.GET_ALL);
		Map<String,Object> maps=new HashMap<String, Object>();
		maps.put("departmentId", this.getLoginUserBySesson(request).getDepartmentId());
		//查询与该用户统同样机构的用户
		List<User> userlist=userService.getList(Constance.GET_ALL, maps);
		
		request.setAttribute("userlist", userlist);
		request.setAttribute("roleList", roleList);
		request.setAttribute("departmentList", departmentList);
		return "SysUser/add";
	}

	@RequestMapping(params = "method=add")
	public String add(@ModelAttribute("form")
	User form, HttpServletRequest request, HttpServletResponse response) {
		
		String message=messageStorage.load(form.getMobilePhone());
		if(StringUtils.isNotEmpty(form.getMessage_code())&&StringUtils.isNotEmpty(message))
		{
			userService.add(Constance.ADD_OBJECT, form);
			return "redirect:/op/SysUserAction.do?method=list";
			
		}
		else
		{
			return "SysUser/add";
		}
		
	
	}

	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("form")
	User form, HttpServletRequest request, HttpServletResponse response) {
		User entity=userService.get(Constance.GET_ONE, form.getId());
		if(entity!=null)
		{
			entity.setDepartmentId(form.getDepartmentId());
			entity.setLeaderId(form.getLeaderId());
			entity.setLoginName(form.getLoginName());
			entity.setPasswd(form.getPasswd());
			entity.setEmail(form.getEmail());
			entity.setMobilePhone(form.getMobilePhone());
			entity.setRoleIds(form.getRoleIds());
			entity.setSex(form.getSex());
			userService.update(Constance.UPDATE_OBJECT, entity);
		}
		return "redirect:/op/SysUserAction.do?method=list";
	}

	@RequestMapping(params = "method=editUI")
	public String editUI(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		User user = userService.get(Constance.GET_ONE, id);
		request.setAttribute("user", user);
		List<Role> roleList = roleService.getAll(Constance.GET_ALL);
		List<Department> departmentList = departmentService
				.getAll(Constance.GET_ALL);
		List<UserRole> userRoleList = userRoleService.getList(
				Constance.GET__LIST_BYUSERID, id);
		
		Map<String,Object> maps=new HashMap<String, Object>();
		maps.put("departmentId",user.getDepartmentId());
		List<User> userlist=userService.getList(Constance.GET_ALL, maps);
		List<Integer> selectList = new ArrayList<Integer>();
		for (UserRole ur : userRoleList) {
			selectList.add(ur.getRoleId());
		}

		// 便利roleList 并且修改参数
		for (Role role : roleList) {
			if (selectList.contains(role.getId())) {
				role.setChecked(true);
			}
		}
		System.out.println(userRoleList);
		request.setAttribute("roleList", roleList);
		request.setAttribute("departmentList", departmentList);
		logger.info(userlist.size());
		logger.info("................."+maps);
		request.setAttribute("userlist", userlist);
		return "SysUser/edit";
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
		return "redirect:/op/SysUserAction.do?method=list";
	}
	
	@RequestMapping(params = "method=report_list")
	public String report_list(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		String livemediaId = request.getParameter("livemediaId");
		request.setAttribute("livemediaId", livemediaId);
		
		return "SysUser/report_list";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=getUserByDepartmentId")
	public String getUserByDepartmentId(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json; charset=utf-8");
		Map<String, Object> hashmap = new HashMap<String, Object>();
		hashmap.put("departmentId", request.getParameter("departmentId"));
		List<User> list = userService.getList(Constance.GET_ALL,
				hashmap);
		JSONObject resp = new JSONObject();
		JSONArray array = new JSONArray();
		for (User user : list) {
			JSONObject object = new JSONObject();
			object.put("id", user.getId());
			object.put("nickname", user.getNickname());
			object.put("loginName", user.getLoginName());
			array.add(object);
		}
		resp.put("rows", array.toString());
		try {
			PrintWriter out = response.getWriter();
			out.print(resp);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=report_list_json")
	public String report_list_json(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/json; charset=utf-8");
		Map<String, Object> hashmap = new HashMap<String, Object>();
		
		/**
		 * 2. limit : 单页多少条记录 3. pageIndex : 第几页，同start参数重复，可以选择其中一个使用
		 */
		hashmap.put("livemediaId", request.getParameter("livemediaId"));
		hashmap.put("startRow", request.getParameter("start") == null ? 0
				: request.getParameter("start"));
		hashmap.put("pageNum",
				request.getParameter("pageIndex").equals("0") ? 1 : request
						.getParameter("pageIndex"));
		hashmap.put("pageSize", request.getParameter("limit").equals("0") ? 1
				: request.getParameter("limit"));
		//统计
		hashmap.put("countNum", ".reportTotalNum");
		PageBean<User> pageBean = userService.queryPage("getReportUsers",
				hashmap);
		List<User> userlist = pageBean.getResultList();
		JSONObject resp = new JSONObject();
		JSONArray array = new JSONArray();
		for (User user : userlist) {
			JSONObject object = new JSONObject();
			object.put("id", user.getId());
			object.put("nickName", user.getNickname());
			object.put("loginName", user.getLoginName());
			object.put("email", user.getEmail());
			object.put("phone", user.getMobilePhone());
			object.put("sex", user.getSex().equals("0") ? "男" : "女");
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
	
	
	
	
	
}
