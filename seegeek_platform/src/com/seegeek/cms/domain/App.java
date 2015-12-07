package com.seegeek.cms.domain;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 12, 2015 4:21:23 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public class App {

	private Integer id;
	//版本
	private String version;
	//渠道 (apple store, 360助手)
	private String channel;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}


}
