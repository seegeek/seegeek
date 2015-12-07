package com.seegeek.cms.androidmessage;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

/**
 * @author aa
 *推送类
 */
public class PullMessage {

    static String appId = "HjAXQV36yy55MF1lbBhYA1";
    static String appkey = "3vTsg1SBsYA7iTipJWvVJ1";
    static String master = "n2XsSh7YcA6hoCMRPFAvs5";
    static String host = "http://sdk.open.api.igexin.com/apiex.htm";
	/**
	 * @return
	 * 透传推送模版
	 */
	public static TransmissionTemplate transmissionTemplateDemo() {
		TransmissionTemplate template = new TransmissionTemplate();
//	    template.setAppId(appId);
//	    template.setAppkey(appkey);
		// 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
		template.setTransmissionType(1);
		template.setTransmissionContent("请输入需要透传的内容");
		// 设置定时展示时间
		// template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
		return template;
	}
	public static TransmissionTemplate transmissionTemplate(String object) {
	    TransmissionTemplate template = new TransmissionTemplate();
	    template.setAppId(appId);
	    template.setAppkey(appkey);
	    // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	    template.setTransmissionType(2);
	    template.setTransmissionContent(object.toString());
	    // 设置定时展示时间
	    // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
	    return template;
	}
	
	 /**
	 * 获取24小时在线用户模版
	 */
	public static void GetOnlineUser() {
			IGtPush push = new IGtPush("3vTsg1SBsYA7iTipJWvVJ1", "n2XsSh7YcA6hoCMRPFAvs5");
	        IQueryResult queryResult = push.getLast24HoursOnlineUserStatistics("HjAXQV36yy55MF1lbBhYA1");
	        System.out.println(queryResult);
	        System.out.println(queryResult.getResponse().get("HjAXQV36yy55MF1lbBhYA1"));
	        System.out.println(queryResult.getResponse().get("onlineStatics"));
	    }
	
	
	public static void main(String[] args) {
		GetOnlineUser();
			
	}
	     
}
