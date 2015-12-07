package com.seegeek.cms.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.seegeek.cms.androidmessage.SSLSocketFactoryEx;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：Jun 6, 2015 12:54:38 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 * 模拟http 请求 发送信息 工具类
 */
public class HttpUtils {

	/**
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String PostInfo(String url,String bodyInfo) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		// TokensService tokenService = new TokensServiceImpl();
		// String tokenInfo = tokenService.getAuthString(5, password,
		// "", tenantName);
		HttpPost httpPost = new HttpPost(url);
		// httpPost.addHeader("X-Auth-Token", OpenstackParam.TOKEN);
		httpPost.addHeader("Accept", "application/json");
		httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
		HttpEntity entity = new StringEntity(bodyInfo);
		System.out.println("【"+bodyInfo);
		httpPost.setEntity(entity);
		HttpResponse res = client.execute(httpPost);
		HttpEntity httpEntity = res.getEntity();
		String responseInfo = "";
		if (res.getStatusLine().getStatusCode() == 200
				|| res.getStatusLine().getStatusCode() == 202) {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(httpEntity.getContent()));
			String line = bufferedReader.readLine();
			while (line != null) {
				responseInfo += line;
				line = bufferedReader.readLine();
			}
			// 将json串封装为json
		}
		else
		{
			System.out.println(res.getStatusLine());
			
		}
		return responseInfo;
	}
	/**
	 * @param random 
	 * @param minute m
	 * @return
	 * @throws Exception
	 */
	public static String POST_SMS(String random,String minute,String to) throws Exception {
		String url="";
		String server="https://api.ucpaas.com";
		String version = "2014-06-30";
		String accountSid="127f24faff40706ba582c883cb587b1e";
		String authToken="a7e8942c9ff9d70852684fd89220d842";
		String appId="3424262e89b14e1aaa98abe1b9c927a2";
		String templateId="12085";
		//MD5加密
		EncryptUtil encryptUtil = new EncryptUtil();
		//获取时间戳
		String timestamp = dateToStr(new Date(), "yyyyMMddHHmmss");//获取时间戳
		String signature =getSignature(accountSid,authToken,timestamp,encryptUtil);
		url=server+"/"+version+"/Accounts/"+accountSid+"/Messages/templateSMS?sig="+signature;
		//body json
		TemplateSMS templateSMS=new TemplateSMS();
		templateSMS.setAppId(appId);
		templateSMS.setTemplateId(templateId);
		templateSMS.setTo(to);
		templateSMS.setParam(random+","+minute);
		Gson gson = new Gson();
		String bodyInfo ="{\"templateSMS\":"+gson.toJson(templateSMS)+"}";
		//post
		HttpClient client = SSLSocketFactoryEx.getNewHttpClient();
		HttpPost httpPost = new HttpPost(url);
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httpPost.setHeader("Authorization", auth);
		httpPost.addHeader("Accept", "application/json");
		httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
		HttpEntity entity = new StringEntity(bodyInfo);
		httpPost.setEntity(entity);
		HttpResponse res = client.execute(httpPost);
		HttpEntity httpEntity = res.getEntity();
		String responseInfo = "";
		if (res.getStatusLine().getStatusCode() == 200
				|| res.getStatusLine().getStatusCode() == 202) {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(httpEntity.getContent()));
			String line = bufferedReader.readLine();
			while (line != null) {
				responseInfo += line;
				line = bufferedReader.readLine();
			}
			// 将json串封装为json
		}
		else
		{
			System.out.println(res.getStatusLine());
			
		}
		return responseInfo;
	}
	/**
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String GetInfo(String url,String bodyInfo) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		// TokensService tokenService = new TokensServiceImpl();
		// String tokenInfo = tokenService.getAuthString(5, password,
		// "", tenantName);
		HttpGet httpGet = new HttpGet(url);
		// httpPost.addHeader("X-Auth-Token", OpenstackParam.TOKEN);
		httpGet.addHeader("Accept", "application/json");
		httpGet.addHeader("Content-Type", "application/json; charset=UTF-8");
		HttpResponse res = client.execute(httpGet);
		HttpEntity httpEntity = res.getEntity();
		String responseInfo = "";
		if (res.getStatusLine().getStatusCode() == 200
				|| res.getStatusLine().getStatusCode() == 202) {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(httpEntity.getContent()));
			String line = bufferedReader.readLine();
			while (line != null) {
				responseInfo += line;
				line = bufferedReader.readLine();
			}
			// 将json串封装为json
		}
		
		return responseInfo;
	}
	public static void main(String[] args) {
		String url = "http://124.126.126.19:8081/seegeek/rest/item/uploadVideo";
//		String url = "http://124.126.126.19/tmp";
//		String url = "http://58.53.2.1919.69:5000/online?RoomId="+"55ad130e2b5e2b0a2690177d";
		try {
			
			String info=PostInfo(url, "{\"Md5\":\"12345678123456\",\"Name\":\"\",\"TotalSize\":0}");
			System.out.println(info);
//			if(org.apache.commons.lang.StringUtils.isNotEmpty(info))
//			{
//			JSONObject json = JSONObject.fromObject(info);
//			JSONArray array=json.getJSONArray("room");
//			List<String> mobileList=new LinkedList<String>();
////			User user=null;
//				if(array!=null&&array.size()>0)
//				{ 
////					//直播转换
//					for(int i=0;i<array.size();i++)
//					{
//						JSONObject object=(JSONObject) array.get(i);
//						System.out.println(object);
////						System.out.println(object);
//						mobileList.add(object.get("name").toString());
////						if(object.get("role").equals("viewer"))
////						{
////							 user = userService.get(Constance.GET_BY_MOBILEPHONE, object.get("name"));
////							 object.put("icon", user.getIcon());
////						}
//					}
////					user
//					System.out.println(mobileList);
//				}
//			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	//转换时间格式
	public static String dateToStr(Date date,String pattern) {
	       if (date == null || date.equals(""))
	    	 return null;
	       SimpleDateFormat formatter = new SimpleDateFormat(pattern);
	       return formatter.format(date);
 } 
	public static String getSignature(String accountSid, String authToken,String timestamp,EncryptUtil encryptUtil) throws Exception  {
		String sig = accountSid + authToken + timestamp;
		String signature = encryptUtil.md5Digest(sig);
		return signature;
	}
}
