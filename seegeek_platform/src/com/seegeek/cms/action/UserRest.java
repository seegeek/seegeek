package com.seegeek.cms.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.seegeek.cms.domain.User;
import com.seegeek.cms.domain.UserLocation;
import com.seegeek.cms.param.Param;
import com.seegeek.cms.redis.MessageStorage;
import com.seegeek.cms.service.IUserLocationService;
import com.seegeek.cms.service.IUserService;
import com.seegeek.cms.utils.HttpUtils;
import com.seegeek.cms.utils.Mail;
import com.seegeek.cms.utils.QRCodeDecoderHandlerUtil;

@Controller
@RequestMapping(value = "/rest")
public class UserRest {
	final static Logger logger = Logger.getLogger(UserRest.class);
	@Autowired
	public IUserService userService;
	@Autowired
	public IUserLocationService userLocationService;
	@Autowired
	public MessageStorage messageStorage;

	@RequestMapping(value = "/title")
	public @ResponseBody
	String printTitle() {
		System.out.println("...........");
		return "title";
	}



	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public @ResponseBody
	Object addUser() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "注册信息");
		return jsonObject;
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
	int getVerification(@RequestBody String body) {
	
		
		com.alibaba.fastjson.JSONObject object=com.alibaba.fastjson.JSONObject.parseObject(body);
		logger.info("body-----"+body);
		String sRand = "";
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
		}
		try {
			
			HttpUtils.POST_SMS(sRand, "2", object.getString("UserId"));
			//bind user messagecode
			User entity=new User();
			entity.setMobilePhone(object.getString("UserId"));
			entity.setLoginName(object.getString("Username"));
			entity=userService.get(Constance.CHECKLOGIN, entity);
			messageStorage.addOrUpdate(entity==null?object.getString("UserId"):entity.getMobilePhone(),sRand);
//			entity.setMessage_code(sRand);
//			logger.info(messageStorage.load(entity.getMobilePhone()));
			return Integer.valueOf(sRand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody
	int register(HttpServletRequest request,@RequestBody String body) {
		com.alibaba.fastjson.JSONObject object=com.alibaba.fastjson.JSONObject.parseObject(body);
		logger.info("body-----"+body);
		User entity = new User();
		entity.setLoginName(object.getString("UserId"));
		entity.setMobilePhone(object.getString("UserId"));
		entity.setPasswd(object.getString("PasswordEn"));
		entity.setIMEI(object.getString("IMEI"));
		// 查询实体信息是否已经存在
		User user = userService.get(Constance.GET_BY_MOBILEPHONE, object.getString("UserId"));
		if (user == null) {
			userService.add(Constance.ADD_OBJECT, entity);
			entity = userService.get(Constance.GET_BY_MOBILEPHONE, entity);
			// logger.info(entity);
			request.getSession().setAttribute("user", entity);
			return 0;
		} else {
			return 1;
		}
	}
	
	@RequestMapping(value = "/checkMobilePhone", method = RequestMethod.POST)
	public @ResponseBody
	int checkMobilePhone(HttpServletRequest request,@RequestBody String body) {
		com.alibaba.fastjson.JSONObject object=com.alibaba.fastjson.JSONObject.parseObject(body);
		logger.info("body-----"+body);
		User entity = new User();
		entity.setMobilePhone(object.getString("UserId"));
		
		// 查询实体信息是否已经存在
		User user = userService.get(Constance.GET_BY_MOBILEPHONE, object.getString("UserId"));
		if (user == null) {
			return 0;
		} else {
			return 1;
		}
	}

	
	@RequestMapping(value = "/registerInfo", method = RequestMethod.POST)
	public @ResponseBody
	int registerInfo(HttpServletRequest request, @RequestParam("UserId")
	String UserId, @RequestParam("Verification")
	String Verification, @RequestParam("PasswordEn")
	String PasswordEn, @RequestParam("IMEI")
	String IMEI,@RequestParam("Language")
	String Language,@RequestParam("Device_os")
	String Device_os,@RequestParam("Device_os_version")
	String Device_os_version,@RequestParam("Idfa")
	String Idfa,@RequestParam("Device_token")
	String Device_token,@RequestParam("Device_name")
	String Device_name,@RequestParam("Cpu")
	String Cpu,@RequestParam("Device_unicode")
	String Device_unicode) {
		User entity = new User();
		entity.setLoginName(UserId);
		entity.setMobilePhone(UserId);
		entity.setPasswd(PasswordEn);
		entity.setIMEI(IMEI);
		
		//设备信息
		Map<String, Object> device=new HashMap<String, Object>();
//		device.setLanguage(Language);
//		device.setDevice_os(Device_os);
//		device.setIdfa(Idfa);
//		device.setDevice_token(Device_token);
//		device.setDevice_name(Device_name);
//		device.setCpu(Cpu);
//		device.setDevice_unicode(Device_unicode);
		//app表 信息需要在 返回token 入库
		// 查询实体信息是否已经存在
		device.put("language", Language);
		device.put("device_os", Device_os);
		device.put("idfa", Idfa);
		device.put("device_os_version", Device_os_version);
		device.put("device_token", Device_token);
		device.put("device_name", Device_name);
		device.put("cpu", Cpu);
		device.put("device_unicode", Device_unicode);
	
	
		User user = userService.get(Constance.GET_BY_MOBILEPHONE, UserId);
		if (user == null) {

			userService.add(Constance.ADD_OBJECT, entity);
			entity = userService.get(Constance.GET_BY_MOBILEPHONE, entity);
			device.put("userId", entity.getId());
			userService.add("addDeviceObject", device);
			// logger.info(entity);
			request.getSession().setAttribute("user", entity);
			return 0;
		} else {
			return 1;
		}
	}

	
	@RequestMapping(value = "/getInfo", method = RequestMethod.GET)
	public @ResponseBody
	String getInfo(HttpServletRequest request) {
		User sessionUser = (User) request.getSession().getAttribute("user");
		User entity = new User();
		// entity.set(IMEI);
		entity.setMobilePhone(sessionUser.getMobilePhone());
		// 查询实体信息是否已经存在
		User user = userService.get(Constance.CHECKLOGIN, entity);
		if (user != null) {
			String json = com.alibaba.fastjson.JSONObject.toJSONString(user);
			// logger.info(json);
			return json;
		}
		return "";
	}

	/**新版可能弃用
	 * @param request
	 * @param RoomId
	 * @return
	 */
	@RequestMapping(value = "/online", method = RequestMethod.GET)
	public @ResponseBody
	String online(HttpServletRequest request, @RequestParam("RoomId")
	String RoomId) {
		String url = "http://" + Param.licode_server + ":5000/api?RoomId="
				+ RoomId;
		try {
			String info = HttpUtils.GetInfo(url, "");
			if (StringUtils.isNotEmpty(info)) {
				JSONObject json = JSONObject.fromObject(info);
				JSONArray array = json.getJSONArray("room");
				System.out.println("array------" + array);
				User user = null;
				if (array != null && array.size() > 0) {
					System.out.println("arraysize------" + array.size());
					// 直播转换
					for (int i = 0; i < array.size(); i++) {
						JSONObject object = (JSONObject) array.get(i);
						if (object.get("role").equals("viewer")) {
							user = userService.get(
									Constance.GET_BY_MOBILEPHONE, object.get(
											"name").toString());
							object.put("icon", user.getIcon());
						}
					}
				}
				System.out.println(array);
				return array.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public @ResponseBody
	int verify(HttpServletRequest request, HttpServletResponse response,@RequestBody String body) {
		logger.info("------"+body);
		com.alibaba.fastjson.JSONObject object=com.alibaba.fastjson.JSONObject.parseObject(body);
	
		User entity = new User();
		entity.setPasswd(object.getString("PasswordEn"));
		entity.setMobilePhone(object.getString("UserId"));
		entity.setIMEI(object.getString("IMEI"));
		entity.setEmail(object.getString("UserId"));
		
		// 查询实体信息是否已经存在
		User user = userService.get(Constance.CHECKLOGIN, entity);
		if (user != null) {
			request.getSession().setAttribute("user", user);
			return 0;
		}
		//按email 查询
		user = userService.get(Constance.CHECKEMAIL, entity);
		if (user != null) {
			request.getSession().setAttribute("user", user);
			return 0;
		}
	
		return 1;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody
	void logout(HttpServletRequest request) {
		request.getSession().removeAttribute("user");
	}


	@RequestMapping(value = "/checkLoginStatus", method = RequestMethod.GET)
	public @ResponseBody
	int checkLoginStatus(HttpServletRequest request,@RequestBody String body) {
	
		
		com.alibaba.fastjson.JSONObject object=com.alibaba.fastjson.JSONObject.parseObject(body);
		logger.info("body-----"+body);
		User sessionUser = (User) request.getSession().getAttribute("user");
		// 查询实体信息是否已经存在
		if (sessionUser != null
				&& sessionUser.getMobilePhone().equals(object.getString("UserId"))) {
			// logger.info("Id--------" + sessionUser.getMobilePhone());
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 
	 * private String latitude; longitude;
	 * 
	 * @param request
	 * @param Icon
	 * @param Sex
	 * @return
	 */

	@RequestMapping(value = "/appCid", method = RequestMethod.POST)
	public @ResponseBody
	int appCid(HttpServletRequest request, @RequestParam("String CID")
	String CID) {
		User sessionUser = (User) request.getSession().getAttribute("user");
		// logger.info("Id--------" + sessionUser.getMobilePhone());
		User entity = userService.get(Constance.GET_ONE, sessionUser.getId());
		if (entity != null) {
			entity.setCid(CID);
			userService.update(Constance.UPDATE_OBJECT, entity);
			return 0;
		}
		return 1;
	}

	@RequestMapping(value = "/careLocation", method = RequestMethod.POST)
	public @ResponseBody
	int careLocation(HttpServletRequest request,@RequestBody String body) {
		com.alibaba.fastjson.JSONObject object=com.alibaba.fastjson.JSONObject.parseObject(body);
		logger.info(body);
		User sessionUser = (User) request.getSession().getAttribute("user");
		// logger.info("Id--------" + sessionUser.getMobilePhone());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("longitude", object.getString("Longitude"));
		map.put("latitude", object.getString("Latitude"));
		map.put("userId", sessionUser.getId());
		int count = userLocationService.queryCount(
				Constance.GET_USER_LOCATIONS_COUNT, map);
		if (count == 0) {
			UserLocation entity = new UserLocation();
			entity.setLatitude(object.getString("Latitude"));
			entity.setLongitude(object.getString("Longitude"));
			entity.setUserId(sessionUser.getId());
			userLocationService.add(Constance.ADD_OBJECT, entity);
			return 0;
		}
		return 1;
	}

	// 绑定邮箱功能,登陆时可以通过邮箱登录

	@RequestMapping(value = "/active_email", method = RequestMethod.GET)
	public String active_email(HttpServletRequest request,@RequestBody String body) {
		com.alibaba.fastjson.JSONObject object=com.alibaba.fastjson.JSONObject.parseObject(body);
		logger.info(body);
		User sessionUser = (User) request.getSession().getAttribute("user");
		// logger.info("Id--------" + sessionUser.getMobilePhone());
		User entity = userService.get(Constance.GET_BY_MOBILEPHONE, sessionUser
				.getMobilePhone());

		if (StringUtils.isNotEmpty(object.getString("Email")) && StringUtils.isNotEmpty(object.getString("Token"))) {
			Double time = (System.currentTimeMillis() - entity.getBind_time()) / 1000 / 60 / 60;
			if (entity.getEmail().equals(object.getString("Email"))
					&& entity.getToken().equals(object.getString("Token"))) {

				if (time <= 24.0) {
					entity.setActive_status("1");
					userService.update(Constance.UPDATE_OBJECT, entity);
					return "Email/success";
				} else {
					logger.info("您的token已过期,请重新申请! ");
					return "Email/timeout";
				}
			}
		}
		return "Email/error";
	}

	@RequestMapping(value = "/bind_email", method = RequestMethod.POST)
	public @ResponseBody
	int bind_email(HttpServletRequest request,@RequestBody String body) {
		com.alibaba.fastjson.JSONObject object=com.alibaba.fastjson.JSONObject.parseObject(body);
		logger.info(body);
		StringBuffer buffer = new StringBuffer();
		User sessionUser = (User) request.getSession().getAttribute("user");
		// logger.info("Id--------" + sessionUser.getMobilePhone());
		String token = System.currentTimeMillis()
				+ UUID.randomUUID().toString().replaceAll("-", "");
		User entity = userService.get(Constance.GET_BY_MOBILEPHONE, sessionUser
				.getMobilePhone());
		entity.setEmail(object.getString("Email") == "" ? entity.getEmail() : object.getString("Email"));
		entity.setToken(token);
		entity.setBind_time(Double.valueOf(System.currentTimeMillis()));
		buffer.append("<p><font style='font-weight:bold;'>Hi,"
				+ entity.getNickname() + "</font></p>");
		buffer.append("<p>seegeek 正在尝试绑定邮箱地址【" + object.getString("Email") + "】到你的账号</p>");
		buffer
				.append("<p>如果这是你的操作，请<a href='http://"+Param.licode_server+":8081/seegeek/rest/active_email?Email="
						+ entity.getEmail()
						+ "&Token="
						+ token
						+ "'+>点击确认</a>完成邮箱绑定</p>");
		buffer.append("<p>如果你没有操作绑定此邮箱，请忽略此邮件</p>");
		Mail.send(Param.SMTP_SERVER, Param.EMAIL_USERNAME, object.getString("Email"), "激活你的邮箱",
				buffer.toString(), Param.EMAIL_USERNAME, Param.EMAIL_PASSWORD);
		userService.update(Constance.UPDATE_OBJECT, entity);
		return 0;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody
	int update(HttpServletRequest request,@RequestBody String body) {
		com.alibaba.fastjson.JSONObject object=com.alibaba.fastjson.JSONObject.parseObject(body);
		logger.info(body);
		User sessionUser = (User) request.getSession().getAttribute("user");
		// logger.info("Id--------" + sessionUser.getMobilePhone());
		User entity = userService.get(Constance.GET_BY_MOBILEPHONE, sessionUser
				.getMobilePhone());
		//
		if (entity != null) {

			try {
				String img = QRCodeDecoderHandlerUtil
						.decoderQRCodeForBase64(object.getString("Icon"));
				entity.setIcon(img);
			} catch (Exception e) {
				e.printStackTrace();
			}
			entity
					.setNickname(object.getString("Nickname") == "" ? entity.getNickname()
							: object.getString("Nickname") );
			entity.setMobilePhone(object.getString("MobilePhone") == "" ? entity.getMobilePhone()
					: object.getString("MobilePhone") );
			entity.setPasswd(object.getString("Passwd") == "" ? entity.getPasswd() : object.getString("Passwd") );
			entity.setPersonal_signature(object.getString("Personal_signature") == "" ? entity
					.getPersonal_signature() : object.getString("Personal_signature") );
			entity.setEmail(object.getString("Email") == "" ? entity.getEmail() : object.getString("Email") );
			entity.setHome_address(object.getString("Home_address") == "" ? entity
					.getHome_address() : object.getString("Home_address") );
			entity.setWork_address(object.getString("Work_address") == "" ? entity
					.getWork_address() : object.getString("Work_address") );
			entity.setSex(object.getString("Sex") == "" ? entity.getSex() : Integer.valueOf(object.getString("Sex")));
			userService.update(Constance.UPDATE_OBJECT, entity);
			return 0;
		}
		return 1;
	}

	/**该功能可能会被废弃
	 * @param Role
	 * @param UserId
	 * @param IMEI
	 * @param RoomName
	 * @param Location
	 * @param Longitude
	 * @param Latitude
	 * @return
	 */
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
		String url = "http://" + Param.licode_server + ":5000/token/";
		String info = "";
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Role", Role);
		map.put("username", UserId);
		map.put("IMEI", IMEI);
		map.put("room_name", RoomName);
		map.put("Longitude", Longitude);
		map.put("Latitude", Latitude);
		JSONObject bodyInfo = JSONObject.fromObject(map);
		System.out.println(bodyInfo);
		try {
			info = HttpUtils.PostInfo(url, bodyInfo.toString());
			System.out.println(info);
			return info;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
