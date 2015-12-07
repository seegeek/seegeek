package com.seegeek.cms.utils;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.seegeek.cms.action.Constance;
import com.seegeek.cms.domain.Resource;
import com.seegeek.cms.domain.User;
import com.seegeek.cms.service.IResourceService;

public class PrivilegeUtils {

	/**
	 * 所有需要控制的URL列表
	 */
	private static List<String> allPrivilegeTagList; //   

	static {
		// 初始化时加载所有的权限URL
	    IResourceService resourceService  = (IResourceService) ContextUtils.getBean("resourceServiceImpl");  
		allPrivilegeTagList = resourceService.getStringList(Constance.GET_STRING_LIST,null);
//		tx.commit();
		System.out.println(allPrivilegeTagList);
//		HibernateUtils.closeCurrentSession();
	}


	/**
	 * 把一个完整的URL变成判断权限所用的URL的格式并返回
	 * 
	 * @param url
	 * @param request
	 * @return
	 */
	public static String getPrivilegeUrl(String url, HttpServletRequest request) {
		// 1，去掉前面的contextPath
		String contextPath = request.getContextPath();
		url = url.substring(contextPath.length());

		// 2，去掉配置的action属性（URL）后的多余参数
		if (url.indexOf("&") != -1) {
			url = url.substring(0, url.indexOf("&"));
		}

		// 3，如果有UI，则去掉
		if (url.endsWith("UI")) {
			url = url.substring(0, url.length() - 2);
		}

		// 4，如果有多个连着的'/'，就替换为一个
		url = url.replaceAll("//+", "/");
		
		return url;
	}


	public static boolean hasButtonPrivilege(User user, Set<Resource> userPrivilegeList, String property) {
		if (!allPrivilegeTagList.contains(property.trim())) {
			return true;
		}
		// 3，超级管理员有所有的权限
		if ("admin".equals(user.getLoginName())) {
			return true;
		}
		// 4，普通用户，得判断一下
		for (Resource resource : userPrivilegeList) {
			if (property.trim().equals(resource.getTag().toString()) ) {
				if(resource.getChildren()==null||resource.getChildren().size()==0)
				{
					resource.setChildren(null);
				}
				return true;
			}
		}
		return false;

	}

}
