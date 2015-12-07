package com.seegeek.cms.domain;

import org.codehaus.jackson.annotate.JsonProperty;


/**
 * @author 作者 zhaogaofei
 * @email 邮箱 zhaogaofei2012@163.com
 * @date 创建时间 Nov 28, 2015 3:18:19 PM
 */
public class Video {
	private Integer id;
	// 名称
	
	@JsonProperty(value="Name")
	private String name;
	// 当前文件大小 
	//别名
	@JsonProperty(value="Md5")
	private String md5;
	// 文件总大小
	@JsonProperty(value="TotalSize")
	private long totalSize;
	// 0 done,1 uploading....
	private String status;
	private String reg_time;
	//uuid
	private String uuid;
	
	private String file_location;

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

	

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReg_time() {
		return reg_time;
	}

	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}

	public String getFile_location() {
		return file_location;
	}

	public void setFile_location(String file_location) {
		this.file_location = file_location;
	}

}
