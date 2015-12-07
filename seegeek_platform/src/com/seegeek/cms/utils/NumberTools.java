package com.seegeek.cms.utils;

import java.util.ArrayList;
import java.util.List;
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
	@SuppressWarnings("unchecked")
	public static List<String> arrayconverToListString(String[] values) { //如果values不为空，且values的长度大于0的话，才进行转换操作
		List<String> list =null;
		
		if (values != null && values.length > 0) {
			list=new ArrayList();
			//循环遍历string里面的数组，并将String数组里面的内容赋值给long数组
			for (int i = 0; i < values.length; i++) {
				list.add(values[i]);
			}
		}
		return list;
	}
	public static List<Integer> arrayconverToListInt(String[] values) { //如果values不为空，且values的长度大于0的话，才进行转换操作
		List<Integer> list=new ArrayList<Integer>();

		if (values != null && values.length > 0) {
		
			//循环遍历string里面的数组，并将String数组里面的内容赋值给long数组
			for (int i = 0; i < values.length; i++) {
				list.add(Integer.valueOf(values[i]));
			}
		}
		return list;
	}
}
