package com.seegeek.cms.domain;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 12, 2015 4:21:23 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public class RoleResource {

	private Integer id;
	//登录名
	private Integer roleId;
	private Integer resourceId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

}
