package com.seegeek.cms.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:50:58 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public class GeneriacUtils {
	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenericType(Class clz) {
		Type type = clz.getGenericSuperclass();
		Type[] at = ((ParameterizedType) type).getActualTypeArguments();
		return (Class) at[0];
	};
}
