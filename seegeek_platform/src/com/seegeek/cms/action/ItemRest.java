package com.seegeek.cms.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.seegeek.cms.androidmessage.PullMessage;
import com.seegeek.cms.domain.Comment;
import com.seegeek.cms.domain.LiveMedia;
import com.seegeek.cms.domain.User;
import com.seegeek.cms.domain.UserLocation;
import com.seegeek.cms.domain.Video;
import com.seegeek.cms.domain.Watch;
import com.seegeek.cms.enumvo.PLAYTYPE;
import com.seegeek.cms.param.Param;
import com.seegeek.cms.service.ICommentService;
import com.seegeek.cms.service.ILiveMediaService;
import com.seegeek.cms.service.IUserLocationService;
import com.seegeek.cms.service.IUserService;
import com.seegeek.cms.service.IVideoService;
import com.seegeek.cms.service.IWatchService;
import com.seegeek.cms.utils.HttpUtils;
import com.seegeek.cms.utils.MD5Util;
import com.seegeek.cms.utils.QRCodeDecoderHandlerUtil;
import com.seegeek.cms.utils.UploadFileUtils;

/**
 */
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
	@Autowired
	public IUserLocationService locationService;
	
	@Autowired
	public IVideoService videoService;
	
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	protected RuntimeService runtimeService;

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
		// logger.info(list);、

		Map<String, List<LiveMedia>> mapContainer = new HashMap<String, List<LiveMedia>>();
		// Set<String> set= new LinkedHashSet<String>();
		// for (LiveMedia live : list) {
		// set.add(live.getStart_date());
		// mapContainer.put(live.getStart_date(), null);
		// // System.out.println(live);
		// // JSONObject object = new JSONObject();
		// // object.put("id", live.getId());
		// // object.put(live.getStart_date(), live.getStart_date());
		// // array.add(object);
		// }
		for (LiveMedia live2 : list) {
			if (mapContainer.get(live2.getStart_date()) != null) {
				mapContainer.get(live2.getStart_date()).add(live2);
			} else {
				List<LiveMedia> medialist = new ArrayList<LiveMedia>();
				medialist.add(live2);
				mapContainer.put(live2.getStart_date(), medialist);
			}

			// JSONObject jsonObject=JSONObject.fromObject(mapContainer);
			// out.println(jsonObject);
			// set.add(live.getStart_date());
			// mapContainer.put(live.getStart_date(), null);
			// System.out.println(live);
			// JSONObject object = new JSONObject();
			// object.put("id", live.getId());
			// object.put(live.getStart_date(), live.getStart_date());
			// array.add(object);
		}

		for (Map.Entry<String, List<LiveMedia>> entry : mapContainer.entrySet()) {
			System.out.println(entry.getKey() + "--" + entry.getValue());
			JSONObject object = new JSONObject();
			JSONArray jsonarray = new JSONArray();
			for (LiveMedia lm : entry.getValue()) {
				JSONObject ob = new JSONObject();
				ob.put("id", lm.getId());
				ob.put("tag", lm.getTag() == null ? "" : lm.getTag());
				jsonarray.add(ob);
			}
			object.put(entry.getKey(), jsonarray);
			// object.put(live.getStart_date(), live.getStart_date());
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
//	String getItemListEntity(HttpServletRequest request, @RequestParam("Offset")
//			int Offset, @RequestParam("Num")
//			int Num) {
	String getItemListEntity(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		map.put("userId", user.getId());
//		map.put("Num",Num);
//		map.put("Offset", Offset);
		JSONArray array = getItemCommons(map);
		return array.toString();
	}

	@RequestMapping(value = "/getItemSource", method = RequestMethod.GET)
	public @ResponseBody
	String getItemSource(HttpServletRequest request, @RequestParam("ItemId")
	String ItemId) {
		// logger.info("itemid---" + ItemId);
		LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE, ItemId);
		if (liveMedia != null) {
			com.alibaba.fastjson.JSONObject object = new com.alibaba.fastjson.JSONObject();
			if (liveMedia.getPlay_type() == 0) {
				object.put("type", "live");
				object.put("value", liveMedia.getRoomId());
			} else if (liveMedia.getPlay_type() == 1) {
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
		JSONArray array = ConvertMediaInfo(list);
		return array;
	}

	private JSONArray ConvertMediaInfo(List<LiveMedia> list) {
		JSONArray array = new JSONArray();
		for (LiveMedia live : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", live.getId());
			jsonObject.put("title", live.getTitle());
			jsonObject.put("seen_num", live.getSeen_num() == null ? 0 : live
					.getSeen_num());
			jsonObject
					.put("type", live.getType() == null ? "" : live.getType());
			jsonObject.put("frame", live.getFrame());
			jsonObject.put("location", live.getLocation() == null ? "" : live
					.getLocation());
			jsonObject.put("describe", live.getDescription());
			jsonObject.put("praise_num", live.getGet_praise_num() == null ? 0
					: live.getGet_praise_num());
			jsonObject.put("comment_num", live.getComment_num() == null ? 0
					: live.getComment_num());
			jsonObject.put("tag", live.getTag());
			jsonObject.put("room_id", live.getRoomId() == null ? "" : live
					.getRoomId());
			jsonObject.put("datatime", live.getStart_time() == null ? "" : live
					.getStart_time());
			// collect number
			jsonObject.put("collect_num", live.getGet_collect_num() == null ? 0
					: live.getGet_collect_num());
			// nickname
			jsonObject.put("nickname", live.getUser() == null ? "" : live
					.getUser().getNickname());
			jsonObject.put("userId", live.getUser() == null ? "" : live
					.getUser().getId());
			// user_icon
			jsonObject.put("icon", live.getUser() == null ? "" : live.getUser()
					.getIcon());
			array.add(jsonObject);
			// logger.info(jsonObject);
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
		//				 map.put("userId", user.getId());
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
	String getItem(HttpServletRequest request, @RequestParam("ItemId")
	String ItemId) {
		JSONObject jsonObject = new JSONObject();
		if (StringUtils.isNotEmpty(ItemId)) {
			User user = (User) request.getSession().getAttribute("user");
			// if(user!=null&&user.getIcon()!=null)
			if (user != null) {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("itemId", ItemId);
				List<LiveMedia> list = liveMediaService.getListByUserId(
						Constance.GET__LIST_BYUSERID, map);
				// logger.info("size -------------------" + list.size());
				if (list != null && list.size() > 0) {
					LiveMedia liveMedia = list.get(0);
					Map<String, Object> maps = new HashMap<String, Object>();
					maps.put("userId", liveMedia.getUser().getId());
					maps.put("fansId", liveMedia.getUser().getId());
					int fansNum = userService.queryCount(
							Constance.GET_FANS_COUNT, maps);
					int careNum = userService.queryCount(
							Constance.GET_USER_CARE_COUNT, maps);
					jsonObject.put("icon",
							liveMedia.getUser().getIcon() == null ? ""
									: liveMedia.getUser().getIcon());
					jsonObject.put("nickname", liveMedia.getUser()
							.getNickname() == null ? "" : liveMedia.getUser()
							.getNickname());
					jsonObject.put("userId",
							liveMedia.getUser().getId() == null ? ""
									: liveMedia.getUser().getId());
					jsonObject.put("personal_signature", liveMedia.getUser()
							.getPersonal_signature() == null ? "" : liveMedia
							.getUser().getPersonal_signature());
					jsonObject.put("fansNum", fansNum);
					jsonObject.put("careNum", careNum);
				}

			} else {
				jsonObject.put("fansNum", "");
				jsonObject.put("icon", "");
				jsonObject.put("nickname", "");
				jsonObject.put("latitude", "");
				jsonObject.put("longitude", "");
			}
			LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE,
					Integer.valueOf(ItemId.trim()));
			// logger.info(ItemId);
			// logger.info(liveMedia + "----------------");
			// logger.info(liveMedia.getLocation() + "-------------------");
			jsonObject.put("latitude", liveMedia.getLatitude() == null ? ""
					: liveMedia.getLatitude());
			jsonObject.put("longitude", liveMedia.getLongitude() == null ? ""
					: liveMedia.getLongitude());
			jsonObject.put("location", liveMedia.getLocation() == null ? ""
					: liveMedia.getLocation());
			jsonObject.put("title", liveMedia.getTitle() == null ? ""
					: liveMedia.getTitle());
			jsonObject.put("describe", liveMedia.getDescription() == null ? ""
					: liveMedia.getDescription());
			jsonObject.put("tag", liveMedia.getTag() == null ? "" : liveMedia
					.getTag());
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
		// map.put("userId", user.getId());
		map.put("livemediaId", Integer.valueOf(ItemId.trim()));
		map.put("Offset", Offset);
		map.put("Num", Num);
		List<Comment> list = commentService.getList(Constance.GET_ALL, map);
		JSONArray array = new JSONArray();
		for (Comment comment : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("nickname", comment.getNickname() == null ? ""
					: comment.getNickname());
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
		// logger.info("fans----------" + maps);
		int count = userService.queryCount(Constance.GET_USER_FANS_COUNT, maps);
		// logger.info("fans count----------" + count);
		if (Integer.valueOf(UserId) != user.getId()) {
			if (count == 0) {
				userService.add(Constance.ADD_USER_FANS, maps);
				return 0;
			}
		}
		return -1;
	}

	@RequestMapping(value = "/getFansList", method = RequestMethod.GET)
	public @ResponseBody
	String getFansList(HttpServletRequest request) {
		Map<String, Object> maps = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		maps.put("fansId", user.getId());
		List<String> list = userService.getListByUserId(
				Constance.GET_USER_CARE, maps);
		if (list != null && list.size() > 0) {
			JSONArray array = selectUsersByList(list);
			return array.toString();
		}
		return "";
	}

	private JSONArray selectUsersByList(List<String> list) {
		List<User> user_list = userService.getList(Constance.GET_USERS_BY_LIST,
				list);
		JSONArray array = new JSONArray();
		for (User userVo : user_list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", userVo.getId() == null ? "" : userVo.getId());
			jsonObject.put("icon", userVo.getIcon() == null ? "" : userVo
					.getIcon());
			jsonObject.put("nickname", userVo.getNickname() == null ? ""
					: userVo.getNickname());
			jsonObject
					.put("personal_signature", userVo.getPersonal_signature());
			array.add(jsonObject);
		}
		return array;
	}

	@RequestMapping(value = "/getAroundItemList", method = RequestMethod.GET)
	public @ResponseBody
	String getAroundListItem(HttpServletRequest request, @RequestParam("Num")
	String Num, @RequestParam("Offset")
	String Offset) {
		Map<String, Object> maps = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		maps.put("userId", user.getId());
		maps.put("Num", Num);
		maps.put("Offset", Offset);
		List<UserLocation> userLocationlist = locationService.getList(
				Constance.GET_USER_LOCATIONS, maps);
		List<LiveMedia> mediaList = new ArrayList<LiveMedia>();
		for (UserLocation userLocation : userLocationlist) {
			maps.put("longitude", userLocation.getLongitude());
			maps.put("latitude", userLocation.getLatitude());
			maps.put("userId", user.getId());
			List<LiveMedia> tempList = liveMediaService.getList(
					Constance.GET_AROUND_LIST_ITEM, maps);
			mediaList.addAll(tempList);
		}
		JSONArray array = ConvertMediaInfo(mediaList);
		// logger.info(mediaList.size());
		return array.toString();
	}

	@RequestMapping(value = "/getCareList", method = RequestMethod.GET)
	public @ResponseBody
	String getCareList(HttpServletRequest request) {
		Map<String, Object> maps = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		maps.put("userId", user.getId());
		List<String> list = userService.getListByUserId(
				Constance.GET_USER_FANS, maps);
		if (list != null && list.size() > 0) {
			JSONArray array = selectUsersByList(list);
			return array.toString();
		}
		return "";
	}

	@RequestMapping(value = "/getCaredItemList", method = RequestMethod.GET)
	public @ResponseBody
	String getCaredItemList(HttpServletRequest request, @RequestParam("Offset")
	int Offset, @RequestParam("Num")
	int Num) {
		Map<String, Object> maps = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("user");
		maps.put("userId", user.getId());
		maps.put("Offset", Offset);
		maps.put("Num", Num);
		List<String> user_list = userService.getListByUserId(
				Constance.GET_USER_FANS, maps);
		maps.remove("userId");
		if (user_list != null && user_list.size() > 0) {
			maps.put("uId", user.getId());
			maps.put("user_list", user_list);
			JSONArray array = getItemCommons(maps);
			return array.toString();
		}
		return "";
	}

	@RequestMapping(value = "/collect", method = RequestMethod.POST)
	public @ResponseBody
	int collect(HttpServletRequest request, @RequestParam("ItemId")
	String ItemId, @RequestParam("B")
	boolean B) {

		LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE, Integer
				.valueOf(ItemId));
		if (liveMedia != null) {
			Integer current = (liveMedia.getGet_collect_num() == null ? 0
					: liveMedia.getGet_collect_num()) + 1;
			liveMedia.setGet_collect_num(current);
			// logger.info(liveMedia + ">>");
			liveMediaService.update(Constance.UPDATE_OBJECT, liveMedia);
		}

		// Map<String, Object> maps = new HashMap<String, Object>();
		// maps.put("livemediaId", Integer.valueOf(ItemId));
		// User user = (User) request.getSession().getAttribute("user");
		// maps.put("userId", user.getId());
		// int count = liveMediaService.getListByUserIdLiveMediaId(
		// Constance.GET_USER_LIVEMEDIA, maps);
		// if (count == 0) {
		// liveMediaService.add(Constance.ADD_USER_LIVEMEDIA, maps);
		// }
		return 0;
	}

	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
	public @ResponseBody
	String uploadImg(HttpServletRequest request, @RequestParam("ImgData")
	String ImgData, @RequestParam("RoomId")
	String RoomId, @RequestParam("RecordingId")
	String RecordingId) {
		LiveMedia entity = new LiveMedia();
		try {
			User user = (User) request.getSession().getAttribute("user");
			String icon = QRCodeDecoderHandlerUtil
					.decoderQRCodeForBase64(ImgData);
			entity.setFrame(icon);
			entity.setRoomId(RoomId == "" ? entity.getRoomId() : RoomId);
			entity.setRecordingId(RecordingId == "" ? entity.getRecordingId()
					: RecordingId + ".mkv");
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

	@RequestMapping(value = "/uploadVideo", method = RequestMethod.POST)
	public @ResponseBody
	String uploadVideo(HttpServletRequest request,@RequestParam(value="Name",required=false)
			String Name,@RequestParam(value="Md5",required=false)
			String Md5,@RequestParam(value="TotalSize",required=false)
			String TotalSize,@RequestBody String body) throws IOException {
		System.out.println("param1....."+Name);
		System.out.println("param2....."+Md5);
		System.out.println("param3....."+Md5);
		com.alibaba.fastjson.JSONObject object=com.alibaba.fastjson.JSONObject.parseObject(body);
		System.out.println("body>>>>>>>>>>>>."+object);
		String md5=object.getString("Md5");
		String name=object.getString("Name");
		String totalSize=object.getString("TotalSize");
		Video v=new Video();
		String serverPath="http://"+Param.licode_server+"/data/";
		JSONObject jo = new JSONObject();
		if(StringUtils.isNotEmpty(name))
		{
			Video entity=videoService.get("getName", name);
			if(entity!=null&&entity.getName()!=null)
			{
				
				File file=new File(serverPath);
				String md5Str= MD5Util.getFileMD5String(file);
				if (!md5.equals(md5Str))
				{
					long f=file.length();
					jo.put("size", f);
					jo.put("name", name);
					jo.put("location", serverPath);
				}
			}
			else
			{
				v.setName(UUID.randomUUID().toString());
				v.setFile_location(serverPath);
				v.setMd5(v.getMd5());
				jo.put("size", 0);
				jo.put("name", v.getName());
				jo.put("location", serverPath);
				videoService.add(Constance.ADD_OBJECT, v);
			}
		}
		else
		{
			v.setName(UUID.randomUUID().toString());
			v.setFile_location(serverPath);
			v.setMd5(v.getMd5());
			jo.put("size", 0);
			jo.put("name", v.getName());
			jo.put("location", serverPath);
			videoService.add(Constance.ADD_OBJECT, v);
		}
	
	
		return jo.toString();
	}
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody
	int upload(HttpServletRequest request) {
		System.out.println("zzzzzzzzz");
		long maxSize = -1;
		int cacheSize = 1 * 1024 * 1024;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		try {

			if (isMultipart) {
				DiskFileItemFactory factory = new DiskFileItemFactory();

				factory.setSizeThreshold(cacheSize);

//				 factory.setRepository(tempDir);

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

						LiveMedia entity = new LiveMedia();
						entity.setFrame(path);
						liveMediaService.add(Constance.ADD_OBJECT, entity);
						Map<String, Object> maps = new HashMap<String, Object>();
						User user = (User) request.getSession().getAttribute(
								"user");
						maps.put("userId", user.getId());
						maps.put("livemediaId", entity.getId());
						liveMediaService
								.add(Constance.ADD_USER_LIVEMEDIA, maps);

					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return 0;
	}
//http://www.cnblogs.com/qq78292959/p/3761646.html
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public @ResponseBody
	int test(HttpServletRequest request) {
//		com.alibaba.fastjson.JSONObject object=com.alibaba.fastjson.JSONObject.parseObject(body);
//		System.out.println(object);
//		String md5=object.getString("Md5");
//		System.out.println(md5);
//		System.out.println(body.getName());
//		System.out.println(body.getMd5());
//		System.out.println(body.getTotalSize());
		
		System.out.println("zzzzzzzzz");
		long maxSize = -1;
		int cacheSize = 1 * 1024 * 1024;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		try {

			if (isMultipart) {
				DiskFileItemFactory factory = new DiskFileItemFactory();

				factory.setSizeThreshold(cacheSize);

//				 factory.setRepository(tempDir);

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

//						LiveMedia entity = new LiveMedia();
//						entity.setFrame(path);
//						liveMediaService.add(Constance.ADD_OBJECT, entity);
//						Map<String, Object> maps = new HashMap<String, Object>();
//						User user = (User) request.getSession().getAttribute(
//								"user");
//						maps.put("userId", user.getId());
//						maps.put("livemediaId", entity.getId());
//						liveMediaService
//								.add(Constance.ADD_USER_LIVEMEDIA, maps);

					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 1;
	}
	@RequestMapping(value = "/getCollectNum", method = RequestMethod.GET)
	public @ResponseBody
	int getCollectNum(HttpServletRequest request, @RequestParam("ItemId")
	String ItemId) {
		LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE, Integer
				.valueOf(ItemId));
		if (liveMedia != null) {
			return liveMedia.getGet_collect_num() == null ? 0 : liveMedia
					.getGet_collect_num();
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
			return liveMedia.getGet_praise_num() == null ? 0 : liveMedia
					.getGet_praise_num();
		} else {
			return 1;
		}
	}

	@RequestMapping(value = "/getSawNum", method = RequestMethod.GET)
	public @ResponseBody
	int getSawNum(@RequestParam("ItemId")
	String ItemId) {
		LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE, Integer
				.valueOf(ItemId));
		if (liveMedia != null) {
			return liveMedia.getSeen_num() == null ? 0 : liveMedia
					.getSeen_num();
		} else {
			return 1;
		}
	}

	@RequestMapping(value = "/saw", method = RequestMethod.POST)
	public @ResponseBody
	int saw(HttpServletRequest request, @RequestParam("ItemId")
	String ItemId, @RequestParam("B")
	boolean B) {
		if (B) {
			LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE,
					Integer.valueOf(ItemId));
			if (liveMedia != null) {
				Integer current = (liveMedia.getSeen_num() == null ? 0
						: liveMedia.getSeen_num()) + 1;
				liveMedia.setSeen_num(current);
				liveMediaService.update(Constance.UPDATE_OBJECT, liveMedia);
			}
		}
		return 0;
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

		LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE, ItemId);
		if (liveMedia != null && liveMedia.getId() != null) {
			liveMedia.setComment_num(liveMedia.getComment_num() == null ? 1
					: liveMedia.getComment_num() + 1);
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
	boolean B,@RequestParam(value="ReportId",required=false)
	String ReportId) {
		if (B) {
			LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE,
					Integer.valueOf(ItemId));
			if (liveMedia != null) {
//				if(liveMedia.getReport_num()==null||liveMedia.getReport_num()==0)
//				{
//					repositoryService.createDeployment().addClasspathResource("task.bpmn").deploy();
//					String processId=runtimeService.startProcessInstanceByKey("myProcess").getId();
//					System.out.println("进程Id...."+processId);
//				}
				Integer current = (liveMedia.getReport_num() == null ? 0
						: liveMedia.getReport_num()) + 1;
				liveMedia.setReport_num(current);
				liveMediaService.update(Constance.UPDATE_OBJECT, liveMedia);
				

				if(StringUtils.isNotEmpty(ReportId))
				{
					User report=userService.get(Constance.GET_ONE, ReportId);
					if(report!=null)
					{
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("reportId", ReportId);
					map.put("mediaId",liveMedia.getId());
					userService.add("addReportInfo", map);
					}
					}

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
			// logger.info(liveMedia.getReport_num());
			return liveMedia.getReport_num();
		} else {
			return -1;
		}
	}

	@RequestMapping(value = "/deleteItem", method = RequestMethod.POST)
	public @ResponseBody
	int deleteItem(@RequestParam("RoomId")
	String RoomId) {
		logger.info("room==" + RoomId);
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("roomId", RoomId);
		LiveMedia entity = new LiveMedia();
		entity.setRoomId(RoomId);
		List<LiveMedia> liveMedia_list = new ArrayList<LiveMedia>();
		liveMedia_list = liveMediaService
				.getList(Constance.GET__ROOMID, entity);
		if (liveMedia_list != null && liveMedia_list.size() > 0) {
			logger.info("delete the " + liveMedia_list.size()
					+ " item by roomId " + RoomId.toString());
			entity.setPlay_type(PLAYTYPE.VOD.ordinal());
			liveMediaService.update(Constance.CONVERT_MEDIA, entity);
		}
		return 0;
	}

	@RequestMapping(value = "/online", method = RequestMethod.GET)
	public @ResponseBody
	String online(HttpServletRequest request, @RequestParam("RoomId")
	String RoomId) {
		// logger.info("RoomId-----" + RoomId);
		String url = "http://" + Param.licode_server + ":5000/online?RoomId="
				+ RoomId;
		try {
			String info = HttpUtils.GetInfo(url, "");
			if (StringUtils.isNotEmpty(info)) {
				JSONObject json = JSONObject.fromObject(info);
				net.sf.json.JSONArray array = json.getJSONArray("room");
				List<String> mobileList = new LinkedList<String>();
				if (array != null && array.size() > 0) {
					for (int i = 0; i < array.size(); i++) {
						JSONObject object = (JSONObject) array.get(i);
						mobileList.add(object.get("name").toString());
					}
					System.out.println(mobileList);
					List<User> userList = userService.getList(
							Constance.GET_LIST_BY_MOBILEPHONE_LIST, mobileList);
					net.sf.json.JSONArray jsonar = new net.sf.json.JSONArray();
					JSONObject jo = null;
					if (userList != null && userList.size() > 0) {
						for (User user : userList) {
							jo = new JSONObject();
							jo.put("nickname", user.getNickname()==null?"":user.getNickname());
							jo.put("icon", user.getIcon()==null?"":user.getIcon());
							jsonar.add(jo);
						}
					}
					return jsonar.toString();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

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
	String Role, @RequestParam("ItemId")
	String ItemId) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			LiveMedia liveMedia = liveMediaService.get(Constance.GET_ONE,
					ItemId);
			if (liveMedia != null && liveMedia.getId() != null) {
				LiveMedia entity = new LiveMedia();
				entity.setTitle(Title == "" ? entity.getTitle() : Title);
				entity.setDescription(Describe == "" ? entity.getDescription()
						: Describe);
				entity.setType(Classes == "" ? entity.getType() : Classes);
				entity.setTag(Tag == "" ? entity.getTag() : Tag);
				entity.setLatitude(Latitude == "" ? entity.getLatitude()
						: Latitude);
				entity.setLongitude(Longitude == "" ? entity.getLongitude()
						: Longitude);
				entity.setLocation(Location == "" ? entity.getLocation()
						: Location);
				entity.setId(Integer.valueOf(ItemId));
				entity.setPlay_type(PLAYTYPE.LIVE.ordinal());
				liveMediaService.update(Constance.UPDATE_OBJECT, entity);
				// String url = "http://" + Param.licode_server +
				// ":5000/api?id="
				// + user.getIMEI() + "&token=token&longitude="
				// + Longitude + "&latitude=" + Latitude + "&Role=" + Role
				// + "&Location=" + Location + "&ItemId=" + entity.getId()
				// + "&nickname=" + user.getNickname() + "&title="
				// + entity.getTitle() + "&tag=" + entity.getTag()
				// + "&icon=" + entity.getFrame();
				// String info = HttpUtils.GetInfo(url, "");

				// notify all the app

				notifyApp(user, entity);
				// get all the user

				// logger.info(url);
				// logger.info("info----" + info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	private void notifyApp(User user, LiveMedia entity) {

		String appId = "HjAXQV36yy55MF1lbBhYA1";
		String appkey = "3vTsg1SBsYA7iTipJWvVJ1";
		String master = "n2XsSh7YcA6hoCMRPFAvs5";
		String host = "http://sdk.open.api.igexin.com/apiex.htm";
		JSONObject object = new JSONObject();
		object.put("title", entity.getTitle() == null ? "" : entity.getTitle());
		object.put("location", entity.getLocation() == null ? "" : entity
				.getLocation());
		object.put("nickname", user.getNickname() == null ? "" : user
				.getNickname());
		object.put("itemId", entity.getId() == null ? "" : entity.getId());
		object.put("icon", entity.getFrame() == null ? "" : entity.getFrame());
		object.put("tag", entity.getTag() == null ? "" : entity.getTag());
		System.out.println(object);
		IGtPush push = new IGtPush(host, appkey, master);
		// 透传模板
		TransmissionTemplate template = PullMessage.transmissionTemplate(object
				.toString());
		AppMessage message = new AppMessage();
		message.setData(template);
		// 设置消息离线，并设置离线时间
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 1000 * 3600);
		// 设置推送目标条件过滤
		List appIdList = new ArrayList();
		List phoneTypeList = new ArrayList();
		appIdList.add(appId);
		// 设置机型
		phoneTypeList.add("ANDROID");
		message.setAppIdList(appIdList);
		message.setPhoneTypeList(phoneTypeList);
		IPushResult ret = push.pushMessageToApp(message);
		System.out.println(ret.getResponse().toString());
	}

	public static void main(String[] args) {
		// User user=new User();
		// user.setNickname("test");
		// LiveMedia liveMedia=new LiveMedia();
		// liveMedia.setLocation("北京市龙锦苑");
		// liveMedia.setTitle("TTT");
		// liveMedia.setId(18);
		// liveMedia.setTag("12");
		// new ItemRest().notifyApp(user, liveMedia);

		String url = "http://58.53.219.69:5000/online?RoomId="
				+ "55ad130e2b5e2b0a2690177d";
		try {  
			String info = HttpUtils.GetInfo(url, "");
			System.out.println(info);
			if (StringUtils.isNotEmpty(info)) {
				JSONObject json = JSONObject.fromObject(info);
				net.sf.json.JSONArray array = json.getJSONArray("room");
				List<String> mobileList = new LinkedList<String>();
				// User user=null;
				if (array != null && array.size() > 0) {
					// //直播转换
					for (int i = 0; i < array.size(); i++) {
						JSONObject object = (JSONObject) array.get(i);
						System.out.println(object);
						// System.out.println(object);
						mobileList.add(object.get("name").toString());
						// if(object.get("role").equals("viewer"))
						// {
						// user = userService.get(Constance.GET_BY_MOBILEPHONE,
						// object.get("name"));
						// object.put("icon", user.getIcon());
						// }
					}
					System.out.println(mobileList);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
