package com.seegeek.cms.domain;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:50:34 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public class Role {

	private String id;

	private String loginName;

	private String mobilePhone;

	private String email;
	
	
	private String IMEI;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String imei) {
		IMEI = imei;
	}
	
	
	
	
}
