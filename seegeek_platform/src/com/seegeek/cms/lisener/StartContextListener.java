package com.seegeek.cms.lisener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartContextListener implements ServletContextListener {
	public void contextDestroyed(ServletContextEvent arg0) {
	}
	public void contextInitialized(ServletContextEvent arg0) {
		try {
//			  Timer timer = new Timer(); 
//		      timer.schedule(new CheckRoomTask(),0, 10*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
