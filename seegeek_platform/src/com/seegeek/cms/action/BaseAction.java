package com.seegeek.cms.action;

import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.seegeek.cms.domain.User;
import com.seegeek.cms.service.ILiveMediaService;
import com.seegeek.cms.service.IUserService;

@Component
public class BaseAction implements  ServletContextAware {

	@Autowired
	public IUserService userService;
	

	@Autowired
	public ILiveMediaService liveMediaService;
	protected static Logger logger =null ;
	protected static String className =null ;
	
	private WebApplicationContext applicationContext;
	
	public BaseAction() {
		super();
		className = getClass().getName();
	    logger=Logger.getLogger(className);
	}
	

    



	public int getPageNo(HttpServletRequest request){
		int pageNo = 1;
		if(null != request.getParameter("page") && !"".equals(request.getParameter("page"))) {
				pageNo = Integer.parseInt(request.getParameter("page"));
		}
		return pageNo;
	}

	public int getPageSize(HttpServletRequest request){
	 int pageSize =  15;
	    if(null != request.getParameter("rp") && !"".equals(request.getParameter("rp"))) {
	        pageSize = Integer.parseInt(request.getParameter("rp"));
    }
    return pageSize;
	}
	
	
	public User getLoginUserBySesson(HttpServletRequest request){
		return (User) request.getSession().getAttribute("user");
	}

	public void setServletContext(ServletContext servletContext) {
//		applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}
	
	public String getMessage(HttpServletRequest request,String messagekey,Object... objs){
		Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
		return applicationContext.getMessage(messagekey, objs, locale);
	}
}