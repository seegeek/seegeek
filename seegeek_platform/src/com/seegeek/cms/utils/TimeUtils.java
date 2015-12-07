package com.seegeek.cms.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 作者 zhaogaofei
 * @email 邮箱 zhaogaofei2012@163.com
 * @date 创建时间 Oct 17, 2015 4:04:28 PM 时间处理类
 */
public class TimeUtils {
	public static void main(String[] args) {
//		calculate_date(1, 1);
		System.out.println(getFirstDate());
		System.out.println(getLastDate());
	}

	/**
	 * 按分钟来计算 0 stand for + 1 stand for -
	 * 
	 * @return
	 */
	public static String calculate_time(int sign, int minutus) {
		Date now = new Date();
		Date time = null;
		if (sign == 0) {
			time = new Date(now.getTime() + minutus * 60 * 1000); // 10分钟后的时间
		} else {
			time = new Date(now.getTime() - minutus * 60 * 1000); // 10分钟前的时间
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		String calculateTime = dateFormat.format(time);
		System.out.println(calculateTime);
		return calculateTime;
	}

	public static String calculate_date(int sign, int date) {
		Date now = new Date();
		Date time = null;
		if (sign == 0) {
			time = new Date(now.getTime() + date * 24 * 60 * 60 * 1000); // date
			// 天的时间
		} else {
			time = new Date(now.getTime() - date * 24 * 60 * 60 * 1000); // date
			// 天的时间
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式
		String calculateDate = dateFormat.format(time);
		System.out.println(calculateDate);
		return calculateDate;
	}

	public static String getCurrentTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(System.currentTimeMillis());
	}

	public static String getCurrentDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(System.currentTimeMillis());
	}

	public static String getCurrentTime_00(String date) {
		DateFormat df = new SimpleDateFormat(date + " 00:00:00");
		return df.format(System.currentTimeMillis());
	}

	public static String getCurrentTime_24(String date) {
		DateFormat df = new SimpleDateFormat(date + " 24:00:00");
		return df.format(System.currentTimeMillis());
	}

	public static String getFirstDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		// 设置为1号,当前日期既为本月第一天
		c.set(Calendar.DAY_OF_MONTH, 1);
		String first = format.format(c.getTime());
		System.out.println("===============first:" + first);
		return first;

	}

	public static String getLastDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca
				.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = format.format(ca.getTime());
		System.out.println("===============last:" + last);
		return last;
	}

}
