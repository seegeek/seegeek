package com.seegeek.cms.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 12, 2015 4:21:23 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
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

	//个推生成的CID
	private String cid;
	
	
	//email token 
	private String token;
	//邮箱绑定时间  时间戳的数字
	private Double bind_time;
	
	//email login  0 can't login,1 can login
	
	private String active_status="";
	
	// 极视币
	private Double bill;
	
	
	//设备
	private Device device;
	//app 
	private App app;
	
	
	//remark 封号原因
	private String lock_remark;
	//lock time
	private String lock_time;
	
	//0 stand for 网页注册的用户
	//1 stand for 能登录后台系统的用户
	private String type;
	
	private List<Role> roleList;
	private List<Integer> roleIds=new ArrayList<Integer>();
	
	//department 
	private Department department;
	private String departmentId;
	
	private String uuid;
	
	private String message_code;
	
	
	//领导,记者拥有自己的领导
	
	private User leader;
	private Integer leaderId;
	
	
	
	public String getMessage_code() {
		return message_code;
	}

	public void setMessage_code(String message_code) {
		this.message_code = message_code;
	}

	public String getLock_remark() {
		return lock_remark;
	}

	public void setLock_remark(String lock_remark) {
		this.lock_remark = lock_remark;
	}

	public String getLock_time() {
		return lock_time;
	}

	public void setLock_time(String lock_time) {
		this.lock_time = lock_time;
	}

	public Double getBill() {
		return bill;
	}

	public void setBill(Double bill) {
		this.bill = bill;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Double getBind_time() {
		return bind_time;
	}

	public void setBind_time(Double bind_time) {
		this.bind_time = bind_time;
	}

	public String getActive_status() {
		return active_status;
	}

	public void setActive_status(String active_status) {
		this.active_status = active_status;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public List<Integer> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public User getLeader() {
		return leader;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	public Integer getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(Integer leaderId) {
		this.leaderId = leaderId;
	}

	

}
