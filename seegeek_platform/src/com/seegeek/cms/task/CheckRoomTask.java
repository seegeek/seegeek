package com.seegeek.cms.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.seegeek.cms.action.Constance;
import com.seegeek.cms.domain.LiveMedia;
import com.seegeek.cms.enumvo.PLAYTYPE;
import com.seegeek.cms.param.Param;
import com.seegeek.cms.service.ILiveMediaService;
import com.seegeek.cms.utils.HttpUtils;

/**
 * @author aa 检测 roomt
 */
public class CheckRoomTask extends TimerTask {
	private ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
	private ILiveMediaService liveMediaService = (ILiveMediaService) ac.getBean("liveMediaServiceImpl");
	public void run() {
		try {
			//查询在线的直播，如果房间人数为0 就转为点播
			String url="";
			String info="";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("play_type", PLAYTYPE.LIVE.ordinal());
			List<LiveMedia> list = liveMediaService.getListByUserId(
					Constance.GET__LIST_BYUSERID, map);
			for(LiveMedia liveMedia:list)
			{
				url = "http://" + Param.licode_server + ":5000/online?RoomId="+liveMedia.getRoomId();
				info = HttpUtils.GetInfo(url, "");
				if(StringUtils.isNotEmpty(info))
				{
				JSONObject json = JSONObject.fromObject(info);
				JSONArray array=json.getJSONArray("room");
					if(array==null||array.size()==0)
					{ 
						//直播转换
						System.out.println("直播roomId"+liveMedia.getRoomId()+"---转点播");
						liveMedia.setPlay_type(PLAYTYPE.VOD.ordinal());
						liveMediaService.update(Constance.CONVERT_MEDIA, liveMedia);
					}
				}
				}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
