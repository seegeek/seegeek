package com.seegeek.cms.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class SmsTemplat {
	public static String version = "2014-06-30";
	public static String server="api.ucpaas.com";
	public static String accountSid="127f24faff40706ba582c883cb587b1e";
	public static String authToken="a7e8942c9ff9d70852684fd89220d842";
	public static String appId="3424262e89b14e1aaa98abe1b9c927a2";
	public static String templateId="12085";
	public static String to="15010215479";
	public static String para="0000,1";

	public static String getSignature(String accountSid, String authToken,String timestamp,EncryptUtil encryptUtil) throws Exception{
		String sig = accountSid + authToken + timestamp;
		String signature = encryptUtil.md5Digest(sig);
		return signature;
	}
	
	public static HttpResponse get(String cType,String accountSid,String authToken,String timestamp,String url,DefaultHttpClient httpclient,EncryptUtil encryptUtil) throws Exception{
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Accept", cType);//
		httpget.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httpget.setHeader("Authorization",auth);
		HttpResponse response = httpclient.execute(httpget);
		return response;
	}
	
	public static HttpResponse post(String cType,String accountSid,String authToken,String timestamp,String url,DefaultHttpClient httpclient,EncryptUtil encryptUtil,String body) throws Exception{
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Accept", cType);
		httppost.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httppost.setHeader("Authorization", auth);
		BasicHttpEntity requestBody = new BasicHttpEntity();
        requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
        requestBody.setContentLength(body.getBytes("UTF-8").length);
        httppost.setEntity(requestBody);
        //查看返回值
		HttpResponse response = httpclient.execute(httppost);
		return response;
	}
	
	//转换时间格式
	public static String dateToStr(Date date,String pattern) {
	       if (date == null || date.equals(""))
	    	 return null;
	       SimpleDateFormat formatter = new SimpleDateFormat(pattern);
	       return formatter.format(date);
 } 
	
	public static String templateSMS(String accountSid, String authToken,
			String appId, String templateId, String to, String param) {
		// TODO Auto-generated method stub
		String result = "";
		DefaultHttpClient httpclient=new DefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			//获取时间戳
			String timestamp = dateToStr(new Date(), "yyyyMMddHHmmss");//获取时间戳
			String signature =getSignature(accountSid,authToken,timestamp,encryptUtil);
			StringBuffer sb = new StringBuffer("https://");
			sb.append(server);
			String url = sb.append("/").append(version)
					.append("/Accounts/").append(accountSid)
					.append("/Messages/templateSMS")
					.append("?sig=").append(signature).toString();
			TemplateSMS templateSMS=new TemplateSMS();
			templateSMS.setAppId(appId);
			templateSMS.setTemplateId(templateId);
			templateSMS.setTo(to);
			templateSMS.setParam(param);
			Gson gson = new Gson();
			String body = gson.toJson(templateSMS);
			body="{\"templateSMS\":"+body+"}";
			System.out.println("post bpdy is: " + body);

			HttpResponse response=post("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil, body);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
				System.out.println("templateSMS Response content is: " + result);
			}
			// 确保HTTP响应内容全部被读出或者内容流被关闭
//			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			//关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	/**
	 * 短信验证码简易接口*/
//	public static String templateSimpleSMS(String accountSid, String authToken,
//			String appId, String templateId, String to, String param) {
//		// TODO Auto-generated method stub
//		String result = "";
//		String url = "http://www.ucpaas.com/maap/sms/code";
//		String timestamp = dateToStr(new Date(), "yyyyMMddHHmmssSSS");//获取时间戳
//		DefaultHttpClient httpclient=new DefaultHttpClient();
//		try {
//			//MD5加密
//			EncryptUtil encryptUtil = new EncryptUtil();
//			String signature =getSignature(accountSid,timestamp,authToken,encryptUtil);
//			StringBuffer sb = new StringBuffer(url);
//			url = sb.append("?")
//					.append("&sid=").append(accountSid)
//					.append("&appId=").append(appId)
//					.append("&time=").append(timestamp)
//					.append("&sign=").append(signature.toLowerCase())
//					.append("&to=").append(to)
//					.append("&templateId=").append(templateId)
//					.append("&param=").append(param).toString();
//			System.out.println("templateSimpleSMS url = "+url);
//			HttpGet httpget = new HttpGet(url);
//
//			HttpResponse response = httpclient.execute(httpget);
//			HttpEntity entity = response.getEntity();
//			if (entity != null) {
//				result = EntityUtils.toString(entity, "UTF-8");
//				System.out.println("templateSimpleSMS Response content is: " + result);
//			}
//			EntityUtils.consume(entity);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally{
//			// 关闭连接
//		    httpclient.getConnectionManager().shutdown();
//		}
//		return result;
//	}
	
	/**
	 * main
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("请输入参数...");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String param=br.readLine();
		String [] params=param.split(" ");
		String method = params[0];
		
		if (method.equals("1")) { //短信验证码 
			templateSMS(accountSid, authToken, appId, templateId, to, para);
		}//else if(method.equals("2")){//短信验证码简易接口
//			templateSimpleSMS(accountSid, authToken,appId, templateId, to, para);
//		}
	}
}
