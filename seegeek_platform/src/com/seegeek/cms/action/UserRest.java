package com.seegeek.cms.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.seegeek.cms.domain.User;
import com.seegeek.cms.param.Param;
import com.seegeek.cms.service.IUserService;
import com.seegeek.cms.utils.HttpUtils;
import com.seegeek.cms.utils.NumberTools;

@Controller
@RequestMapping(value = "/rest")
public class UserRest {
	final static Logger logger = Logger.getLogger(UserRest.class);
	@Autowired
	public IUserService userService;


	@RequestMapping(value = "/title")
	public @ResponseBody
	String printTitle() {
		System.out.println("...........");
		return "title";
	}

	@RequestMapping(value = "/publish")
	public @ResponseBody
	String publish() {
		System.out.println("...........");
		return "publish";
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public @ResponseBody
	Object addUser() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "注册信息");
		return jsonObject;
	}

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public @ResponseBody
	String hello() {
		return "print  hello";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("welcome"); // 返回的文件名
		mav.addObject("message", "hello kitty");
		// List
		List<String> list = new ArrayList<String>();
		list.add("java");
		list.add("c++");
		list.add("oracle");
		mav.addObject("bookList", list);
		// Map
		Map<String, String> map = new HashMap<String, String>();
		map.put("zhangsan", "北京");
		map.put("lisi", "上海");
		map.put("wangwu", "深圳");
		mav.addObject("map", map);
		return mav;
	}

	@RequestMapping(value = "logout")
	public ModelAndView logout() {
		String message = "欢迎下次光临！";
		return new ModelAndView("logout", "message", message);
	}

	@RequestMapping(value = "welcome")
	public ModelAndView welcome() {
		String message = "欢迎下次光临！";
		return new ModelAndView("index", "message", message);
	}

	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public ModelAndView printMessage() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("message", "MVC");
		mv.setViewName("users");
		return mv;
	}

	@RequestMapping("/showUser")
	public String toIndex(HttpServletRequest request, Model model) {
		System.out.println("--------------");
		return "showUser";
	}

	@RequestMapping(value = "/adduser", method = RequestMethod.POST)
	public @ResponseBody
	Object addUser(User user) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "注册人员信息成功");
		return jsonObject;
	}

	@RequestMapping(value = "/getItemList")
	public @ResponseBody
	String getItemList(@RequestParam("title")
	String title) {
		System.out.println(title);
		return "title";
	}

	/***************************************************************************
	 * 以上为测试demo
	 * 以下为正式功能================================================================
	 */

	/**
	 * @param userId
	 *            用户的id
	 * @param imei
	 *            用户的唯一码
	 * @return
	 */
	@RequestMapping(value = "/getVerification", method = RequestMethod.GET)
	public @ResponseBody
	int getVerification(@RequestParam("UserId")
	String UserId, @RequestParam("IMEI")
	String IMEI) {
		int number = Integer.valueOf(NumberTools.random(4));
		return number;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody
	int register(HttpServletRequest request,@RequestParam("UserId")
	String UserId, @RequestParam("Verification")
	String Verification, @RequestParam("PasswordEn")
	String PasswordEn, @RequestParam("IMEI")
	String IMEI) {
		User entity = new User();
		entity.setLoginName(UserId);
		entity.setMobilePhone(UserId);
		entity.setPasswd(PasswordEn);
		entity.setIMEI(IMEI);
		// 查询实体信息是否已经存在
		User user = userService.get(Constance.GET_BY_MOBILEPHONE, UserId);
		if (user == null) {
			userService.add(Constance.ADD_OBJECT, entity);
			entity=userService.get(Constance.GET_BY_MOBILEPHONE, entity);
			logger.info(entity);
			request.getSession().setAttribute("user", entity);
			return 0;
		} else {
			return 1;
		}
	}

	@RequestMapping(value = "/getInfo", method = RequestMethod.GET)
	public @ResponseBody
		String getInfo(HttpServletRequest request) {
		User sessionUser=(User) request.getSession().getAttribute("user");
		User entity = new User();
//		entity.set(IMEI);
		entity.setMobilePhone(sessionUser.getMobilePhone());
		// 查询实体信息是否已经存在
		User user = userService.get(Constance.CHECKLOGIN, entity);
		if (user != null) {
			String json=com.alibaba.fastjson.JSONObject.toJSONString(user);
			logger.info(json);
			return json;
		}
		return ""; 
	}
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public @ResponseBody
	int verify(HttpServletRequest request,@RequestParam("UserId")
			String UserId, @RequestParam("PasswordEn")
			String PasswordEn, @RequestParam("IMEI")
			String IMEI) {
		User entity = new User();
		entity.setPasswd(PasswordEn);
		entity.setMobilePhone(UserId);
		entity.setIMEI(IMEI);
		// 查询实体信息是否已经存在
		User user = userService.get(Constance.CHECKLOGIN, entity);
		if (user != null) {
			request.getSession().setAttribute("user", user);
			return 0;
		} else {
			return 1;
		}
	}
	@RequestMapping(value = "/checkLoginStatus", method = RequestMethod.GET)
	public @ResponseBody
	int checkLoginStatus(HttpServletRequest request,@RequestParam("UserId")
	String UserId) {
		User sessionUser=(User) request.getSession().getAttribute("user");
		// 查询实体信息是否已经存在
		if (sessionUser != null&&sessionUser.getMobilePhone().equals(UserId.trim())) {
		logger.info("Id--------"+sessionUser.getMobilePhone());
			return 0;
		} else {
			return 1;
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody
	int update(HttpServletRequest request,@RequestParam("Nickname")
	String Nickname, @RequestParam("MobilePhone")
	String MobilePhone, @RequestParam("Passwd")
	String Passwd, @RequestParam("Personal_signature")
	String Personal_signature, @RequestParam("Email")
	String Email, @RequestParam("Home_address")
	String Home_address, @RequestParam("Work_address")
	String Work_address, @RequestParam("Icon")
	String Icon, @RequestParam("Sex")
	String Sex) {
		User sessionUser=(User) request.getSession().getAttribute("user");
		logger.info("Id--------"+sessionUser.getMobilePhone());
		User entity=userService.get(Constance.GET_BY_MOBILEPHONE,sessionUser.getMobilePhone());
		//
		if(entity!=null)
		{
			entity.setNickname(Nickname==""?entity.getNickname():Nickname);
			entity.setMobilePhone(MobilePhone==""?entity.getMobilePhone():MobilePhone);
			entity.setPasswd(Passwd==""?entity.getPasswd():Passwd);
			entity.setPersonal_signature(Personal_signature==""?entity.getPersonal_signature():Personal_signature);
			entity.setEmail(Email==""?entity.getEmail():Email);
			entity.setHome_address(Home_address==""?entity.getHome_address():Home_address);
			entity.setWork_address(Work_address==""?entity.getWork_address():Work_address);
			entity.setIcon(Icon==""?entity.getIcon():Icon);
			entity.setSex(Sex==""?entity.getSex():Integer.valueOf(Sex));
			userService.update(Constance.UPDATE_OBJECT, entity);
			return 0;
		}
		else
		{
			return 1;
		}
	}

	@RequestMapping(value = "/requestToken", method = RequestMethod.POST)
	public @ResponseBody
	String requestToken(@RequestParam("Role")
	String Role, @RequestParam("UserId")
	String UserId, @RequestParam("IMEI")
	String IMEI, @RequestParam("RoomName")
	String RoomName, @RequestParam("Location")
	String Location, @RequestParam("Longitude")
	String Longitude, @RequestParam("Latitude")
	String Latitude) {
		String url = "http://"+Param.licode_server+":5000/token/";
		String info = "";
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Role", Role);
		map.put("username", UserId);
		map.put("IMEI", IMEI);
		map.put("room_name", RoomName);
		map.put("Longitude", Longitude);
		map.put("Latitude", Latitude);
		//		map.put("longitude", longitude);
		//		map.put("latitude", latitude);
		JSONObject bodyInfo = JSONObject.fromObject(map);
		System.out.println(bodyInfo);
		try {
			info = HttpUtils.PostInfo(url,bodyInfo.toString());
			System.out.println(info);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO
		return info;
	}

}
