package com.seegeek.cms.domain;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:50:34 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public class Role {

		/**
		 * 组织也分为上下级的关系
		 * 如果用户为admin可以创建一级菜单
		 * 如果为非admin 用户就无法创建菜单
		 */
	private Integer id;

	private String name;

	private String description;
	
	//默认不选中
	private boolean checked=false;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	
	
	
	
}
