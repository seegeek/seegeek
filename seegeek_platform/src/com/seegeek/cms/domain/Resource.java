package com.seegeek.cms.domain;

import java.util.List;
import java.util.Set;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:50:34 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public class Resource {

	private int id;

	private String name;

	private String url;

	private String tag;

	private String icon;

	private String type;
	private Resource parent;
	private Integer parentId;
	private List<Resource> children;
	public Resource() {
	}

	public Resource(String name, String url, String icon,String tag,Integer parentId) {
		this.url = url;
		this.name = name;
		this.icon = icon;
		this.tag= tag;
		this.parentId = parentId;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List<Resource> getChildren() {
		return children;
	}

	public void setChildren(List<Resource> children) {
		this.children = children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Resource other = (Resource) obj;
		if (id != other.id)
			return false;
		return true;
	}




}
