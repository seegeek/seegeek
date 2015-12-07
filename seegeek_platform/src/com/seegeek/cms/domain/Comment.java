package com.seegeek.cms.domain;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：Jun 8, 2015 10:47:57 AM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 * 节目评论
 */
public class Comment {

	private Integer id;

	private String content;

	private String datetime;

	private Integer livemediaId;
	
	private Integer userId;
	
	private  String nickname;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
