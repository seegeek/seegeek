package com.seegeek.cms.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.seegeek.cms.domain.OPUser;
import com.seegeek.cms.domain.User;
import com.seegeek.cms.utils.PrivilegeUtils;

public class OPLoginFilter implements Filter {
	final static Logger logger = Logger.getLogger(OPLoginFilter.class);
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		String url = req.getRequestURI();
		// 获取当前访问的URL，当前登录用户
		String method = req.getParameter("method"); // add, addUI, <null>
		if (method != null) {
			url = url + "?method=" + method;
		}
		url = PrivilegeUtils.getPrivilegeUrl(url, req); // 处理一下这个URL，获取正确格式的资源URL

		
							// 1，如果没有登录
							if (user == null) {
								// 如果正在登录，就放行
								if (url.startsWith("/op/SysUserAction.do?method=login")) { // login, loginUI
									chain.doFilter(request, response);
								}
							
								// 如果不是使用登录，就转到登录页面
								else {
									resp.sendRedirect(req.getContextPath() + "/login.jsp");
//									request.getRequestDispatcher("/op/UserAction.do?method=loginUI").forward(request, response);
								}
							}
							// 2，如果已登录
							else {
								// 如果有权限，就放行
//								Set<Resource> userPrivilegeList = (Set<Resource>) req.getSession().getAttribute("menuList");
//								if (PrivilegeUtils.hasPrivilege(user, userPrivilegeList, url)) {
//									// 放行
									chain.doFilter(request, response);
//								} else {
//									// 如果没权限，就提示没有权限
//									resp.sendRedirect(req.getContextPath() + "/noPrivilege.jsp");
//								}
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
