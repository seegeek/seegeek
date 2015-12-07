package com.seegeek.cms.utils;

import java.util.Random;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：Jun 7, 2015 3:19:25 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 * 产生4位随机数
 */
public class NumberTools {
	public  static String random(int count) {
		StringBuffer sb = new StringBuffer();
		String str = "0123456789";
		Random r = new Random();
		for (int i = 0; i < count; i++) {
			int num = r.nextInt(str.length());
			sb.append(str.charAt(num));
			str = str.replace((str.charAt(num) + ""), "");
		}
		return sb.toString();
	}
}
