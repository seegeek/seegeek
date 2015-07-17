package com.seegeek.cms.param;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Param {
	private static Properties properties;
	/**
	 * 存储路径
	 */
	public static String upload_filepath="";
	public static String licode_server="";
	static {
		InputStream inputStream = null;
		try {
			inputStream = Param.class.getClassLoader().getResourceAsStream("jdbc.properties");
			properties = new Properties();
			properties.load(inputStream);
			upload_filepath = properties.getProperty("upload_filepath");
			licode_server = properties.getProperty("licode_server");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
	
	
