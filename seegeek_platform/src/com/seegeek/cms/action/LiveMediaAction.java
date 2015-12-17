package com.seegeek.cms.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.seegeek.cms.domain.LiveMedia;
import com.seegeek.cms.domain.User;
import com.seegeek.cms.enumvo.PLAYTYPE;
import com.seegeek.cms.param.Param;
import com.seegeek.cms.utils.PageBean;
import com.seegeek.cms.utils.TimeUtils;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 10, 2015 2:56:16 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
@Controller
@RequestMapping("/op/LiveMediaAction.do")
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

	@RequestMapping(params = "method=monitorUI")
	public String monitorUI(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> maps=new HashMap<String, Object>();
		maps.put("play_type", String.valueOf(PLAYTYPE.LIVE.ordinal()));
		List<LiveMedia> entityList = liveMediaService.getList(Constance.GET_ALL,maps);
		request.setAttribute("entityList", entityList);
		request.setAttribute("play_type", String.valueOf(PLAYTYPE.LIVE.ordinal()));
		String name=request.getParameter("name");
		request.setAttribute("name", name);
		return "LiveMedia/monitorUI";
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

	@RequestMapping(params = "method=live_list")
	public String live_list(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		User user = getLoginUserBySesson(request);
		String time=request.getParameter("time");
		String location=request.getParameter("location");
		String nickname=request.getParameter("nickname");
		String title=request.getParameter("title");
		request.setAttribute("time", time);
		request.setAttribute("nickname", nickname);
		request.setAttribute("location", location);
		request.setAttribute("title", title);
		request.setAttribute("play_type", "0");
		return "LiveMedia/live_index";
	}
	
	@RequestMapping(params = "method=vod_list")
	public String vod_list(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		User user = getLoginUserBySesson(request);
		String time=request.getParameter("time");
		String nickname=request.getParameter("nickname");
		String location=request.getParameter("location");
		String title=request.getParameter("title");
		request.setAttribute("time", time);
		request.setAttribute("nickname", nickname);
		request.setAttribute("location", location);
		request.setAttribute("title", title);
		request.setAttribute("play_type", "1");
		return "LiveMedia/vod_index";
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
		String time= request.getParameter("time");
		String location= request.getParameter("location");
		String nickname= request.getParameter("nickname");
		String play_type= request.getParameter("play_type");
		String report_num= request.getParameter("report_num");
		Map<String,String> timeMap=new HashMap<String, String>();
		if(StringUtils.isNotEmpty(time))
		{
			timeMap=convertTime(time);
		}
		hashmap.put("nickname", nickname);
		hashmap.put("start_time", timeMap.get("start_time"));
		hashmap.put("end_time", timeMap.get("end_time"));
		hashmap.put("play_type", play_type);
		hashmap.put("report_num", report_num);
		hashmap.put("startRow", request.getParameter("start")==null?0:request.getParameter("start"));
		hashmap.put("name", request.getParameter("title")==null?"":request.getParameter("title"));
		hashmap.put("location", location==null?"":location);
		hashmap.put("pageNum", request.getParameter("pageIndex").equals("0")?1:request.getParameter("pageIndex"));
		hashmap.put("pageSize", request.getParameter("limit").equals("0")?1:request.getParameter("limit"));
		PageBean<LiveMedia> pageBean= liveMediaService.queryPage(Constance.GET_ALL, hashmap);
		List<LiveMedia> liveMedialist=pageBean.getResultList();
		JSONObject resp=new JSONObject();
		JSONArray array = new JSONArray();
		for(LiveMedia entity:liveMedialist)
		{
			JSONObject object=new JSONObject();
			object.put("id", entity.getId());
			object.put("title", entity.getTitle());
			object.put("tag", entity.getTag());
			object.put("description", entity.getDescription());
			object.put("location", entity.getLocation());
			object.put("type", entity.getType());
			object.put("roomId", entity.getRoomId());
			object.put("recordingId", entity.getRecordingId());
			object.put("start_time", entity.getStart_time());
			object.put("frame", entity.getFrame());
			object.put("report_num",entity.getReport_num());
			object.put("nickname",entity.getUser()==null?"":entity.getUser().getNickname());
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
	
	
	/**
	 * @param time
	 * @return
	 */
	private Map<String,String> convertTime(String time) {
		Map<String,String> map=new HashMap<String, String>();
		int key=Integer.valueOf(time.toString());
		String start_time="";
		String end_time="";
		switch (key) {
		case 1:
			start_time=TimeUtils.calculate_time(1, 10);
			end_time=TimeUtils.getCurrentTime();
			break;
		case 2:
			start_time=TimeUtils.calculate_time(1, 60);
			end_time=TimeUtils.getCurrentTime();
			break;
		case 3:
			start_time=TimeUtils.getCurrentTime_00(TimeUtils.getCurrentDate());
			end_time=TimeUtils.getCurrentTime_24(TimeUtils.getCurrentDate());
			break;
		case 4:
			String date=TimeUtils.calculate_date(1, 1);
			start_time=TimeUtils.getCurrentTime_00(date);
			end_time=TimeUtils.getCurrentTime_24(date);
			break;
		case 5:
			String first_date=TimeUtils.getFirstDate();
			String last_date=TimeUtils.getLastDate();
			start_time=TimeUtils.getCurrentTime_00(first_date);
			end_time=TimeUtils.getCurrentTime_24(last_date);
			break;

		default:
			break;
		}
		map.put("start_time", start_time);
		map.put("end_time", end_time);
		return map;
	}

	@RequestMapping(params = "method=clear")
	public String clear(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		liveMediaService.delete(Constance.DELETE_ALL,"1");
		return "redirect:/LiveMediaAction.do?method=list";
	}
	
	
	@RequestMapping(params = "method=delete")
	public String delete(ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Integer id = Integer.valueOf(request.getParameter("id"));
		String play_type= request.getParameter("play_type");
		LiveMedia liveMedia=liveMediaService.get(Constance.GET_ONE, id);
		if(liveMedia!=null)
		{
			File file=new File(Param.upload_filepath+liveMedia.getFrame());
			if(file.exists())
			{
				file.delete();
			}
			liveMediaService.delete(Constance.DELETE_OBJECT, id);
		}
		if(play_type.equals("1"))
		{
			return "redirect:/op/LiveMediaAction.do?method=vod_list";
		}
		else
		{
			
			return "redirect:/op/LiveMediaAction.do?method=live_list";
		}
		
	}

}
