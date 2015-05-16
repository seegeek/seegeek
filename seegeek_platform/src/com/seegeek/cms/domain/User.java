package com.seegeek.cms.domain;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 12, 2015 4:21:23 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public class User {

	private String id;
	//登录名
	private String loginName;
	//昵称
	private String nickname;
	//手机号
	private String mobilePhone;

	//密码
	private String passwd;

	//个性签名
	private String personal_signature;
	//邮箱
	private String email;
	//手机唯一吗
	private String IMEI;

	private String location_x;

	private String location_y;

	//家庭地址
	private String home_address;
	//工作地址
	private String work_address;
	//头像
	private String icon;
	//性别
	private Integer sex = 0;
	//状态
	private Integer state = 0;

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

	public String getLocation_x() {
		return location_x;
	}

	public void setLocation_x(String location_x) {
		this.location_x = location_x;
	}

	public String getLocation_y() {
		return location_y;
	}

	public void setLocation_y(String location_y) {
		this.location_y = location_y;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getPersonal_signature() {
		return personal_signature;
	}

	public void setPersonal_signature(String personal_signature) {
		this.personal_signature = personal_signature;
	}

	public String getHome_address() {
		return home_address;
	}

	public void setHome_address(String home_address) {
		this.home_address = home_address;
	}

	public String getWork_address() {
		return work_address;
	}

	public void setWork_address(String work_address) {
		this.work_address = work_address;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
	

}
