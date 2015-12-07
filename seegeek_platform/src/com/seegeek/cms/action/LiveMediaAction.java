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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.seegeek.cms.domain.LiveMedia;
import com.seegeek.cms.domain.User;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 10, 2015 2:56:16 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
@Controller
@RequestMapping("/LiveMediaAction.do")
public class LiveMediaAction extends BaseAction {
	/**
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "method=addLiveMedia")
	public String addLiveMedia(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		//直播信息
		LiveMedia entity = new LiveMedia();
		System.out.println(request.getParameter("duration"));
		System.out.println(request.getAttribute("duration"));
		entity.setTitle(request.getParameter("title"));
		entity.setTag(request.getParameter("tag"));
		entity.setDescription(request.getParameter("description"));
		entity.setLocation(request.getParameter("location"));
		entity.setType(request.getParameter("type"));
		liveMediaService.add(Constance.ADD_OBJECT, entity);
		return "index";
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

	@RequestMapping(params = "method=list")
	public String list(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		User user = getLoginUserBySesson(request);
		List<LiveMedia> entityList = liveMediaService.getAll(Constance.GET_ALL);
		request.setAttribute("entityList", entityList);
		return "LiveMedia/index";
	}
	@RequestMapping(params = "method=clear")
	public String clear(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		liveMediaService.delete(Constance.DELETE_ALL,"1");
		return "redirect:/LiveMediaAction.do?method=list";
	}

}
