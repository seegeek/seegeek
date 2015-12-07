package com.seegeek.cms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.seegeek.cms.domain.User;

/**
 * @author 作者 zhaogaofei
 * @email 邮箱 zhaogaofei2012@163.com
 * @date 创建时间 Sep 12, 2015 11:31:21 AM
 * 拦截器-------
 */
public class CommonInterceptor implements HandlerInterceptor {
	private final static Logger log = Logger.getLogger(CommonInterceptor.class);
	public static final String LAST_PAGE = "com.alibaba.lastPage";

	/* 
	 * 利用正则映射到需要拦截的路径     
	 
	private String mappingURL; 
	
	public void setMappingURL(String mappingURL) {     
	          this.mappingURL = mappingURL;     
	}    
	 */
	/**  
	 * 在业务处理器处理请求之前被调用  
	 * 如果返回false  
	 *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 
	 * 如果返回true  
	 *    执行下一个拦截器,直到所有的拦截器都执行完毕  
	 *    再执行被拦截的Controller  
	 *    然后进入拦截器链,  
	 *    从最后一个拦截器往回执行所有的postHandle()  
	 *    接着再从最后一个拦截器往回执行所有的afterCompletion()  
	 */

	/**  
	 * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等   
	 *   
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()  
	 */
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("==============执行顺序: 3、afterCompletion================");
	}

	/** 
	 * 在业务处理器处理请求执行完成后,生成视图之前执行的动作    
	 * 可在modelAndView中加入数据，比如当前时间 
	 */
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("==============执行顺序: 2、postHandle================");
		if (modelAndView != null) { //加入当前时间    
			modelAndView.addObject("var", "测试postHandle");
		}
	}

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());

		log.info("requestUri:" + requestUri);
		log.info("contextPath:" + contextPath);
		log.info("url:" + url);
//		User user = (User) request.getSession().getAttribute("opUser");
//		if (user == null) {
//			log.info("Interceptor：跳转到login页面！");
//			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(
//					request, response);
//			return false;
//		} else
//			return true;
//	}
		return true;
	}

}
