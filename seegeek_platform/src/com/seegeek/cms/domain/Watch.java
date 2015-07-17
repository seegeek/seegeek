package com.seegeek.cms.domain;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：Jun 8, 2015 12:01:46 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 * 收藏表
 */
public class Watch {
	/**
	 * 主键字段
	 */
	private Integer id;
	
	private Integer livemediaId;
	
	private Integer userId;
	
	private String watchTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getLivemediaId() {
		return livemediaId;
	}

	public void setLivemediaId(Integer livemediaId) {
		this.livemediaId = livemediaId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getWatchTime() {
		return watchTime;
	}

	public void setWatchTime(String watchTime) {
		this.watchTime = watchTime;
	}
	
	
	
	
}
