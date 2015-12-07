package com.seegeek.cms.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.seegeek.cms.domain.User;

public class LoginFilter implements Filter {
	final static Logger logger = Logger.getLogger(LoginFilter.class);
	private static List<String> priList=new ArrayList<String>();
	static
	{
		priList.add("getVerification");
		priList.add("verify");
		priList.add("register");
		priList.add("checkLoginStatus");
		priList.add("getPublicItemListEntity");
		priList.add("getComment");
		priList.add("getCommentNum");
		priList.add("getPraiseNum");
		priList.add("getCollectNum");
		priList.add("saw");
		priList.add("getSawNum");
		priList.add("deleteItem");
		priList.add("getItemSource");
		priList.add("getItem");
		priList.add("getSearchList");
		priList.add("online");
		priList.add("active_email");
		priList.add("uploadVideo");
		priList.add("test");
//		
//		comment -- 评论动作
//		praise -- 点赞动作
//		collect -- 收藏动作
//		follow -- 关注人动作
//		report -- 举报动作
	}
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		String url = req.getRequestURI();
			if (url.indexOf("rest") > -1) {

			if (user != null) {// 登录后才能访问
				chain.doFilter(request, response);
			} else {
				if(hasPrivellge(url,priList))
				{
					chain.doFilter(request, response);
				}
			}
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
	/**检查是否有权限
	 * @param list
	 * @return
	 */
	private boolean hasPrivellge(String url,List<String> list)
	{
		for(String str:list)
		{
			 if(url.indexOf(str)>-1)
			 {
				 return true;
			 }
			
		}
		
	return false;	
	}

}
