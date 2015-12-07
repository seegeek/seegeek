package com.seegeek.cms.domain;

/**
 * @author 作者 zhaogaofei
 * @email 邮箱 zhaogaofei2012@163.com
 * @date 创建时间 Nov 23, 2015 1:53:36 PM
 */
public class Device {

	private Integer id;
	//国际化 
	private String language;
	//操作系统
	private String device_os;
	//系统版本
	private String device_os_version;
	//唯一码
	private String idfa;
	//设备token
	private String device_token;
	//设备名称
	private String device_name;
	//cpu 参数
	private String cpu;
	//设备UUID
	private String device_unicode;
	
	private String reg_time;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getDevice_os() {
		return device_os;
	}
	public void setDevice_os(String device_os) {
		this.device_os = device_os;
	}
	public String getDevice_os_version() {
		return device_os_version;
	}
	public void setDevice_os_version(String device_os_version) {
		this.device_os_version = device_os_version;
	}
	public String getIdfa() {
		return idfa;
	}
	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}
	public String getDevice_token() {
		return device_token;
	}
	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getDevice_unicode() {
		return device_unicode;
	}
	public void setDevice_unicode(String device_unicode) {
		this.device_unicode = device_unicode;
	}
	public String getReg_time() {
		return reg_time;
	}
	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}


}
