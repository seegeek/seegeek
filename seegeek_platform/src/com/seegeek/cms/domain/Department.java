package com.seegeek.cms.domain;

import java.util.List;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:50:34 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public class Department {

	private Integer id;

	private String name;

	private String description;
	private Department parent;
	private Integer parentId;
	private List<Department> children;
	
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

	public Department getParent() {
		return parent;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List<Department> getChildren() {
		return children;
	}

	public void setChildren(List<Department> children) {
		this.children = children;
	}

}
