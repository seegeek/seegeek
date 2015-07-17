package com.seegeek.cms.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.seegeek.cms.domain.Comment;
import com.seegeek.cms.domain.LiveMedia;
import com.seegeek.cms.domain.User;
import com.seegeek.cms.domain.Watch;
import com.seegeek.cms.enumvo.PLAYTYPE;
import com.seegeek.cms.param.Param;
import com.seegeek.cms.service.ICommentService;
import com.seegeek.cms.service.ILiveMediaService;
import com.seegeek.cms.service.IUserService;
import com.seegeek.cms.service.IWatchService;
import com.seegeek.cms.utils.HttpUtils;
import com.seegeek.cms.utils.QRCodeDecoderHandlerUtil;
import com.seegeek.cms.utils.UploadFileUtils;

@Controller
@RequestMapping(value = "/rest/item")
public class ItemRest {
	final static Logger logger = Logger.getLogger(ItemRest.class);
	@Autowired
	public IUserService userService;
	@Autowired
	public ILiveMediaService liveMediaService;
	@Autowired
	public ICommentService commentService;
	@Autowired
	public IWatchService watchService;

	@SuppressWarnings("unused")
	@RequestMapping(value = "/getPublishedList", method = RequestMethod.GET)
	public @ResponseBody
	String getPublishedList(HttpServletRequest request, @RequestParam("UserId")
	String UserId) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		map.put("userId", user.getId());
		JSONArray array = new JSONArray();
		List<LiveMedia> list = liveMediaService.getListByUserId(
				Constance.GET__LIST_BYUSERID, map);
		logger.info(list);
		
		

		
		Map<String,List<LiveMedia>> mapContainer= new HashMap<String, List<LiveMedia>>();
//		Set<String> set= new LinkedHashSet<String>();
//		for (LiveMedia live : list) {
//			set.add(live.getStart_date());
//			mapContainer.put(live.getStart_date(), null);
////			System.out.println(live);
////			JSONObject object = new JSONObject();
////			object.put("id", live.getId());
////			object.put(live.getStart_date(), live.getStart_date());
////			array.add(object);
//		}
		for (LiveMedia live2 : list) {
			if(mapContainer.get(live2.getStart_date())!=null)
			{
				mapContainer.get(live2.getStart_date()).add(live2);
			}
			else
			{
				List<LiveMedia> medialist=new ArrayList<LiveMedia>();
				medialist.add(live2);
				mapContainer.put(live2.getStart_date(),medialist);
			}
			
		
		
//			JSONObject jsonObject=JSONObject.fromObject(mapContainer);
//			out.println(jsonObject);
//			set.add(live.getStart_date());
//			mapContainer.put(live.getStart_date(), null);
//			System.out.println(live);
//			JSONObject object = new JSONObject();
//			object.put("id", live.getId());
//			object.put(live.getStart_date(), live.getStart_date());
//			array.add(object);
		}
		
		for (Map.Entry<String,List<LiveMedia>> entry : mapContainer.entrySet()) 
		{
			System.out.println(entry.getKey()+"--"+entry.getValue());
			JSONObject object = new JSONObject();
			JSONArray  jsonarray = new JSONArray();
			for(LiveMedia lm:entry.getValue())
			{
				JSONObject ob = new JSONObject();
				ob.put("id", lm.getId());
				ob.put("tag", lm.getTag()==null?"":lm.getTag());
				jsonarray.add(ob);
			}
			object.put(entry.getKey(), jsonarray);
//			object.put(live.getStart_date(), live.getStart_date());
			array.add(object);
			
		}
		
		
		return array.toString();
	}

	@RequestMapping(value = "/getWatchedList", method = RequestMethod.GET)
	public @ResponseBody
	String getWatchedList(HttpServletRequest request) {
		JSONArray array = new JSONArray();
		Map<String, Object> maps = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		maps.put("userId", user.getId());
		List<Watch> list = watchService.getList(Constance.GET_ALL, maps);
		for (Watch watch : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", watch.getLivemediaId());
			jsonObject.put("time", watch.getWatchTime());
			array.add(jsonObject);
		}
		return array.toString();
	}

	@RequestMapping(value = "/getWatchedNum", method = RequestMethod.GET)
	public @ResponseBody
	int getWatchedNum(HttpServletRequest request) {
		Map<String, Object> maps = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		maps.put("userId", user.getId());
		int count = watchService.queryCount(Constance.GET_WATCH_COUNT, maps);
		return count;
	}

	@RequestMapping(value = "/getItemList", method = RequestMethod.GET)
	public @ResponseBody
	String getItemList(HttpServletRequest request, @RequestParam("SortTag")
	int SortTag, @RequestParam("Offset")
	int Offset, @RequestParam("Num")
	int Num) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		map.put("userId", user.getId());
		List<LiveMedia> list = liveMediaService.getListByUserId(
				Constance.GET__LIST_BYUSERID, map);
		JSONArray array = new JSONArray();
		for (LiveMedia live : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", live.getId());
			array.add(jsonObject);
		}
		return array.toString();
	}
	
	@RequestMapping(value = "/getItemListEntity", method = RequestMethod.GET)
	public @ResponseBody
	String getItemListEntity(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		map.put("userId", user.getId());
		JSONArray array = getItemCommons(map);
		return array.toString();
	}
	@RequestMapping(value = "/getItemSource", method = RequestMethod.GET)
	public @ResponseBody
	String getItemSource(HttpServletRequest request, @RequestParam("ItemId")
			String ItemId) {
		logger.info("itemid---"+ItemId);
		LiveMedia liveMedia=liveMediaService.get(Constance.GET_ONE, ItemId);
		if(liveMedia!=null)
		{
			com.alibaba.fastjson.JSONObject object=new com.alibaba.fastjson.JSONObject();
			if(liveMedia.getPlay_type()==0)
			{
			object.put("type", "live");
			object.put("value", liveMedia.getRoomId());
			}
			else if(liveMedia.getPlay_type()==1)
			{
			object.put("type", "vod");
			object.put("value", liveMedia.getRecordingId());
			}
			return object.toString();
		}
		return "";
	}

	@RequestMapping(value = "/getPublicItemListEntity", method = RequestMethod.GET)
	public @ResponseBody
	String getPublicItemListEntity(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("play_type", PLAYTYPE.LIVE.ordinal());
		JSONArray array = getItemCommons(map);
		return array.toString();
	}

	private JSONArray getItemCommons(Map<String, Object> map) {
		List<LiveMedia> list = liveMediaService.getListByUserId(
				Constance.GET__LIST_BYUSERID, map);
		JSONArray array = new JSONArray();
		for (LiveMedia live : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", live.getId());
			jsonObject.put("title", live.getTitle());
			jsonObject.put("seen_num", live.getSeen_num() == null ? 0 : live
					.getSeen_num());
			jsonObject
					.put("type", live.getType() == null ? "" : live.getType());
			jsonObject.put("icon", live.getIcon());
			jsonObject.put("location", live.getLocation() == null ? "" : live
					.getLocation());
			jsonObject.put("describe", live.getDescription());
			jsonObject.put("praise_num", live.getGet_praise_num() == null ? 0
					: live.getGet_praise_num());
			jsonObject.put("comment_num", live.getComment_num() == null ? 0
					: live.getComment_num());
			jsonObject.put("tag", live.getTag());
			jsonObject.put("room_id", live.getRoomId()==null?"":live.getRoomId());
			jsonObject.put("datatime", live.getStart_time()==null?"":live.getStart_time());
			//collect number
			jsonObject.put("collect_num", live.getGet_collect_num() == null ? 0
					: live.getGet_collect_num());
			//nickname
			jsonObject.put("nickname", live.getUser()==null?"":live.getUser().getNickname());
			
			//user_icon
			jsonObject.put("user_icon", live.getUser()==null?"":live.getUser().getIcon());
			array.add(jsonObject);
			logger.info(jsonObject);
		}
		return array;
	}

	@RequestMapping(value = "/getSearchList", method = RequestMethod.GET)
	public @ResponseBody
	String getSearchList(HttpServletRequest request, @RequestParam("Keyword")
	String Keyword, @RequestParam("Offset")
	int Offset, @RequestParam("Num")
	int Num) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
//		map.put("userId", user.getId());
		map.put("title", Keyword);
		JSONArray array = getItemCommons(map);
		
		return array.toString();
	}

	@RequestMapping(value = "/getRoomId", method = RequestMethod.GET)
	public @ResponseBody
	String getRoomId(HttpServletRequest request, @RequestParam("ItemId")
	String ItemId) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("roomId", "1asdfasdfsadf2");
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("livemediaId", Integer.valueOf(ItemId));
		User user = (User) request.getSession().getAttribute("user");
		maps.put("userId", user.getId());
		int count = watchService.queryCount(Constance.GET_WATCH_COUNT, maps);

		Map<String, Object> mediaMap = new HashMap<String, Object>();
		mediaMap.put("livemediaId", Integer.valueOf(ItemId));
		LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE, Integer
				.valueOf(ItemId));
		if (liveMedia != null) {
			int current = liveMedia.getSeen_num() == null ? 0 : liveMedia
					.getSeen_num();
			liveMedia.setSeen_num(current + 1);
			liveMediaService.update(Constance.UPDATE_OBJECT, liveMedia);
		}
		if (count == 0) {
			Watch watch = new Watch();
			watch.setUserId(user.getId());
			watch.setLivemediaId(Integer.valueOf(ItemId.trim()));
			watchService.add(Constance.ADD_OBJECT, watch);
		}
		return "";
	}

	@RequestMapping(value = "/getItem", method = RequestMethod.GET)
	public @ResponseBody
	String getItem(@RequestParam("ItemId")
	String ItemId) {
		JSONObject jsonObject = new JSONObject();
		if (StringUtils.isNotEmpty(ItemId)) {
			LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE,
					Integer.valueOf(ItemId.trim()));
			logger.info(ItemId);
			logger.info(liveMedia+"----------------");
			logger.info(liveMedia.getLocation()+"-------------------");
			jsonObject.put("thumbnail", "");
			jsonObject.put("location", liveMedia.getLocation());
			jsonObject.put("title", liveMedia.getTitle());
			jsonObject.put("seen_num", liveMedia.getSeen_num() == null ? 0
					: liveMedia.getSeen_num());
			jsonObject.put("describe", liveMedia.getDescription());
			jsonObject.put("tag", liveMedia.getTag());
		}
		return jsonObject.toString();
	}

	@RequestMapping(value = "/getComment", method = RequestMethod.GET)
	public @ResponseBody
	String getComment(HttpServletRequest request, @RequestParam("ItemId")
	String ItemId, @RequestParam("Offset")
	int Offset, @RequestParam("Num")
	int Num) {
		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("userId", user.getId());
		map.put("livemediaId", Integer.valueOf(ItemId.trim()));
		map.put("Offset", Offset);
		map.put("Num", Num);
		List<Comment> list = commentService.getList(Constance.GET_ALL, map);
		JSONArray array = new JSONArray();
		for (Comment comment : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("nickname", comment.getNickname()==null?"":comment.getNickname());
			jsonObject.put("userId", comment.getUserId());
			jsonObject.put("time", comment.getDatetime() == null ? " "
					: comment.getDatetime());
			jsonObject.put("content", comment.getContent());
			array.add(jsonObject);
		}
		return array.toString();
	}

	@RequestMapping(value = "/care", method = RequestMethod.POST)
	public @ResponseBody
	int care(HttpServletRequest request, @RequestParam("UserId")
	String UserId, @RequestParam("B")
	boolean B) {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("fansId", Integer.valueOf(UserId));
		User user = (User) request.getSession().getAttribute("user");
		maps.put("userId", user.getId());
		int count = userService.queryCount(Constance.GET_USER_FANS_COUNT, maps);
		if (count == 0) {
			userService.add(Constance.ADD_USER_FANS, maps);
			return 0;
		}
		return 0;
	}

	@RequestMapping(value = "/getFansList", method = RequestMethod.GET)
	public @ResponseBody
	String getFansList(HttpServletRequest request) {
		Map<String, Object> maps = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		maps.put("userId", user.getId());
		List<String> list = userService.getListByUserId(
				Constance.GET_USER_FANS, maps);
		JSONArray array = new JSONArray();
		for (String id : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", id);
			array.add(jsonObject);
		}

		return array.toString();
	}

	@RequestMapping(value = "/collect", method = RequestMethod.POST)
	public @ResponseBody
	int collect(HttpServletRequest request, @RequestParam("ItemId")
	String ItemId, @RequestParam("B")
	boolean B) {
		
		LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE,
				Integer.valueOf(ItemId));
		if (liveMedia != null) {
			Integer current = (liveMedia.getGet_collect_num() == null ? 0
					: liveMedia.getGet_collect_num()) + 1;
			liveMedia.setGet_collect_num(current);
			logger.info(liveMedia + ">>");
			liveMediaService.update(Constance.UPDATE_OBJECT, liveMedia);
		}
		
//		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("livemediaId", Integer.valueOf(ItemId));
//		User user = (User) request.getSession().getAttribute("user");
//		maps.put("userId", user.getId());
//		int count = liveMediaService.getListByUserIdLiveMediaId(
//				Constance.GET_USER_LIVEMEDIA, maps);
//		if (count == 0) {
//			liveMediaService.add(Constance.ADD_USER_LIVEMEDIA, maps);
//		}
		return 0;
	}

	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
	public @ResponseBody
	String uploadImg(HttpServletRequest request,@RequestParam("ImgData")
	String ImgData,
	@RequestParam("RoomId")
	String RoomId,
	@RequestParam("RecordingId")
	String RecordingId) {
		LiveMedia entity=new LiveMedia();
		try {
			User user = (User) request.getSession().getAttribute("user");
			String icon = QRCodeDecoderHandlerUtil.decoderQRCodeForBase64(ImgData);
			entity.setIcon(icon);
			entity.setRoomId(RoomId==""?entity.getRoomId():RoomId);
			entity.setRecordingId(RecordingId==""?entity.getRecordingId():RecordingId+".mkv");
			entity.setPlay_type(PLAYTYPE.LIVE.ordinal());
			liveMediaService.add(Constance.ADD_OBJECT, entity);
			Map<String, Object> maps = new HashMap<String, Object>();
			maps.put("livemediaId", entity.getId());
			maps.put("userId", user.getId());
			liveMediaService.add(Constance.ADD_USER_LIVEMEDIA, maps);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("ItemId", entity.getId());
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody
	int upload(HttpServletRequest request) {
		long maxSize = -1;
		int cacheSize = 1 * 1024 * 1024;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		try {

			if (isMultipart) {
				DiskFileItemFactory factory = new DiskFileItemFactory();

				factory.setSizeThreshold(cacheSize);

				// factory.setRepository(tempDir);

				ServletFileUpload sfu = new ServletFileUpload(factory);

				sfu.setFileSizeMax(maxSize);

				sfu.setSizeMax(-1);

				sfu.setHeaderEncoding("UTF-8");

				FileItemIterator fii = sfu.getItemIterator(request);

				while (fii.hasNext()) {
					FileItemStream fis = fii.next();
					if (!fis.isFormField() && fis.getName().length() > 0) {
						String path = UploadFileUtils.saveUploadFile(request,
								fis);
						// ======================================
		
						LiveMedia entity=new LiveMedia();
						entity.setIcon(path);
						liveMediaService.add(Constance.ADD_OBJECT, entity);
						Map<String, Object> maps = new HashMap<String, Object>();
						User user = (User) request.getSession().getAttribute("user");
						maps.put("userId", user.getId());
						maps.put("livemediaId", entity.getId());
						liveMediaService.add(Constance.ADD_USER_LIVEMEDIA, maps);
						
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	
		return 0;
	}

	@RequestMapping(value = "/getCollectNum", method = RequestMethod.GET)
	public @ResponseBody
	int getCollectNum(HttpServletRequest request, @RequestParam("ItemId")
	String ItemId) {
		LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE, Integer
				.valueOf(ItemId));
		if (liveMedia != null) {
			return liveMedia.getGet_collect_num()==null?0:liveMedia.getGet_collect_num();
		} else {
			return 1;
		}
		
	}

	@RequestMapping(value = "/praise", method = RequestMethod.POST)
	public @ResponseBody
	int parise(@RequestParam("ItemId")
	String ItemId, @RequestParam("B")
	boolean B) {
		if (B) {
			LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE,
					Integer.valueOf(ItemId));
			if (liveMedia != null) {
				Integer current = (liveMedia.getGet_praise_num() == null ? 0
						: liveMedia.getGet_praise_num()) + 1;
				liveMedia.setGet_praise_num(current);
				liveMediaService.update(Constance.UPDATE_OBJECT, liveMedia);
			}
		}
		return 0;
	}

	@RequestMapping(value = "/getPraiseNum", method = RequestMethod.GET)
	public @ResponseBody
	int getPraiseNum(@RequestParam("ItemId")
	String ItemId) {
		LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE, Integer
				.valueOf(ItemId));
		if (liveMedia != null) {
			return liveMedia.getGet_praise_num()==null?0:liveMedia.getGet_praise_num();
		} else {
			return 1;
		}
	}

	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public @ResponseBody
	int comment(HttpServletRequest request, @RequestParam("ItemId")
	String ItemId, @RequestParam("Content")
	String Content, @RequestParam("B")
	boolean B) {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("livemediaId", Integer.valueOf(ItemId));
		User user = (User) request.getSession().getAttribute("user");
		maps.put("userId", user.getId());
		maps.put("content", Content);
	
		commentService.add(Constance.ADD_COMMENT, maps);
		//将LiveMeida 的comment 数据+1 //临时修改
		
		LiveMedia liveMedia=liveMediaService.get(Constance.GET_ONE, ItemId);
		if(liveMedia!=null&&liveMedia.getId()!=null)
		{
			liveMedia.setComment_num(liveMedia.getComment_num()==null?1:liveMedia.getComment_num()+1);
			liveMediaService.update(Constance.UPDATE_OBJECT, liveMedia);
			
		}

		return 0;
	}

	@RequestMapping(value = "/getCommentNum", method = RequestMethod.GET)
	public @ResponseBody
	int getCommentNum(@RequestParam("ItemId")
	String ItemId) {

		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("livemediaId", Integer.valueOf(ItemId));
		int count = commentService.queryCount(Constance.GET_COMMENT_USERID,
				maps);
		return count;
	}

	@RequestMapping(value = "/report", method = RequestMethod.POST)
	public @ResponseBody
	int report(@RequestParam("ItemId")
	String ItemId, @RequestParam("B")
	boolean B) {
		if (B) {
			LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE,
					Integer.valueOf(ItemId));
			if (liveMedia != null) {
				Integer current = (liveMedia.getReport_num() == null ? 0
						: liveMedia.getReport_num()) + 1;
				liveMedia.setReport_num(current);
				liveMediaService.update(Constance.UPDATE_OBJECT, liveMedia);
			}
		}
		return 0;
	}


	@RequestMapping(value = "/getReportNum", method = RequestMethod.GET)
	public @ResponseBody
	int getReportNum(@RequestParam("ItemId")
			String ItemId) {
		LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE, Integer
				.valueOf(ItemId));
		if (liveMedia != null) {
			logger.info(liveMedia.getReport_num());
			return liveMedia.getReport_num();
		} else {
			return -1;
		}
	}
	@RequestMapping(value = "/deleteItem", method = RequestMethod.POST)
	public @ResponseBody
	int deleteItem(@RequestParam("RoomId") String RoomId) {
		logger.info("room=="+RoomId);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("roomId", RoomId);
		LiveMedia entity=new LiveMedia();
		entity.setRoomId(RoomId);
		List<LiveMedia> liveMedia_list=new ArrayList<LiveMedia>();
		liveMedia_list=liveMediaService.getList(Constance.GET__ROOMID, entity);
		if(liveMedia_list!=null&&liveMedia_list.size()>0)
		{	
	    logger.info("delete the "+liveMedia_list.size()+" item by roomId "+RoomId.toString());
		entity.setPlay_type(PLAYTYPE.VOD.ordinal());
		liveMediaService.update(Constance.CONVERT_MEDIA, entity);
		}
		return 0;
	}

	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	public @ResponseBody
	int publish(HttpServletRequest request, @RequestParam("UserId")
	String UserId, @RequestParam("Token")
	String Token, @RequestParam("Title")
	String Title, @RequestParam("Describe")
	String Describe, @RequestParam("Classes")
	String Classes, @RequestParam("Tag")
	String Tag, @RequestParam("Location")
	String Location, @RequestParam("Longitude")
	String Longitude, @RequestParam("Latitude")
	String Latitude, 

	@RequestParam("Role")
	String Role,
	@RequestParam("ItemId")
	String ItemId
	) {
		try {
			logger.info("UserId-----"+UserId+"--Token---"+Token+"Role----------"+Role);
			User user = (User) request.getSession().getAttribute("user");
			LiveMedia liveMedia=liveMediaService.get(Constance.GET_ONE,ItemId);
			if(liveMedia!=null&&liveMedia.getId()!=null)
			{
			LiveMedia entity = new LiveMedia();
			entity.setTitle(Title==""?entity.getTitle():Title);
			entity.setDescription(Describe==""?entity.getDescription():Describe);
			entity.setType(Classes==""?entity.getType():Classes);
			entity.setTag(Tag==""?entity.getTag():Tag);
			entity.setLocation(Location==""?entity.getLocation():Location);
			entity.setId(Integer.valueOf(ItemId));
			entity.setPlay_type(PLAYTYPE.LIVE.ordinal());
			liveMediaService.update(Constance.UPDATE_OBJECT, entity);
			String url = "http://"+Param.licode_server+":5000/api?id="+user.getIMEI()+"&token=token&longitude="+Longitude+"&latitude="+Latitude+"&Role="+Role;
			String info =  HttpUtils.GetInfo(url,"");
			System.out.println(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
}
