package com.seegeek.cms.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

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
		else
		{
			System.out.println(res.getStatusLine());
			
		}
		return responseInfo;
	}

}
