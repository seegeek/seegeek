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
	//SNMP server
	public static String SMTP_SERVER="";
	//public email or origin
	public static String FROM="";
	//username
	public static String EMAIL_USERNAME="";
	//password
	public static String  EMAIL_PASSWORD="";
	static {
		InputStream inputStream = null;
		try {
			inputStream = Param.class.getClassLoader().getResourceAsStream("jdbc.properties");
			properties = new Properties();
			properties.load(inputStream);
			upload_filepath = properties.getProperty("upload_filepath");
			licode_server = properties.getProperty("licode_server");
			SMTP_SERVER = properties.getProperty("mail.snmp");
			FROM = properties.getProperty("mail.from");
			EMAIL_USERNAME = properties.getProperty("mail.username");
			EMAIL_PASSWORD = properties.getProperty("mail.password");
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
	
	
