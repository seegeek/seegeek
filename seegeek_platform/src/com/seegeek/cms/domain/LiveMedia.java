package com.seegeek.cms.domain;

import java.util.List;

/**
 * @author 作者 zhaogaofei
 * @version 创建时间：May 12, 2015 4:21:23 PM
 * @email zhaogaofei2012@163.com 类说明 直播对象信息
 */
/**
 * @author aa
 *
 */
public class LiveMedia {

	/**
	 * 主键字段
	 */
	private Integer id;
	/**
	 * 时长 单位秒
	 */
	private Long duration;
	/**
	 * 直播起开时间
	 */
	private String start_time;
	/**
	 * 结束时间
	 */
	private String end_time;
	/**
	 * 直播标题
	 */
	private String title;
	/**
	 * 直播标签
	 * 
	 */
	private String tag;
	/**
	 * 直播描述
	 */
	private String description;
	/**
	 * 直播地理位置
	 */
	private String location;
	
	/**
	 * 纬度
	 */
	private String latitude;
	/**
	 * 经度
	 */
	private String longitude;
	/**
	 * 直播类别
	 */
	private String type;

	/**
	 * 已看人数
	 */
	private Integer seen_num;
	/**
	 * 正在看人数
	 */
	private Integer seening_num;

	/**
	 * 已看用户名
	 */
	private String seen_username;
	/**
	 * 正看用户名
	 */
	private String seening_username;
	
	
	/**
	 * 举报数量
	 */
	private Integer report_num;

	/**
	 * 举报用户名
	 */
	private String report_username;

	/**
	 * 获赞数量
	 */
	private Integer get_praise_num;
	
	/**
	 * 收藏量 临时添加
	 */
	
	private Integer get_collect_num;
	/**
	 * 获喜欢数量
	 */
	private Integer get_like_num;
	/**
	 * 评论数量
	 */
	private Integer comment_num;

	
	private String roomId;
	
	private String frame;
	//0是直播 1 是点播
	private Integer play_type;
	//
	private String recordingId;
	//
	private String start_date;
	
	//发布者的userid
	private Integer publisherId;
	//发布者对象
	private User publisher;
	private User user;
	
	private List<User> reportList;
	
	
	
	//审批信息,一个视频可能会有多个审批详情
	private List<ApproveInfo> approveInfos;
	
	//因此需要将审批状态记录到 媒体信息中
	
	private String approve_status;
	
	
	//processInstanceId,activiti 实例Id
	private String processInstanceId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSeen_num() {
		return seen_num;
	}

	public void setSeen_num(Integer seen_num) {
		this.seen_num = seen_num;
	}

	public Integer getSeening_num() {
		return seening_num;
	}

	public void setSeening_num(Integer seening_num) {
		this.seening_num = seening_num;
	}

	public String getSeen_username() {
		return seen_username;
	}

	public void setSeen_username(String seen_username) {
		this.seen_username = seen_username;
	}

	public String getSeening_username() {
		return seening_username;
	}

	public void setSeening_username(String seening_username) {
		this.seening_username = seening_username;
	}

	public Integer getReport_num() {
		return report_num;
	}

	public void setReport_num(Integer report_num) {
		this.report_num = report_num;
	}

	public String getReport_username() {
		return report_username;
	}

	public void setReport_username(String report_username) {
		this.report_username = report_username;
	}

	public Integer getGet_praise_num() {
		return get_praise_num;
	}

	public void setGet_praise_num(Integer get_praise_num) {
		this.get_praise_num = get_praise_num;
	}

	public Integer getGet_like_num() {
		return get_like_num;
	}

	public void setGet_like_num(Integer get_like_num) {
		this.get_like_num = get_like_num;
	}

	public Integer getComment_num() {
		return comment_num;
	}

	public void setComment_num(Integer comment_num) {
		this.comment_num = comment_num;
	}




	/**
	 * @return the frame
	 */
	public String getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	public void setFrame(String frame) {
		this.frame = frame;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public Integer getPlay_type() {
		return play_type;
	}

	public void setPlay_type(Integer play_type) {
		this.play_type = play_type;
	}

	public String getRecordingId() {
		return recordingId;
	}

	public void setRecordingId(String recordingId) {
		this.recordingId = recordingId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getGet_collect_num() {
		return get_collect_num;
	}

	public void setGet_collect_num(Integer get_collect_num) {
		this.get_collect_num = get_collect_num;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}


 
	public List<User> getReportList() {
		return reportList;
	}

	public void setReportList(List<User> reportList) {
		this.reportList = reportList;
	}

	public Integer getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Integer publisherId) {
		this.publisherId = publisherId;
	}

	public List<ApproveInfo> getApproveInfos() {
		return approveInfos;
	}

	public void setApproveInfos(List<ApproveInfo> approveInfos) {
		this.approveInfos = approveInfos;
	}

	public String getApprove_status() {
		return approve_status;
	}

	public void setApprove_status(String approve_status) {
		this.approve_status = approve_status;
	}

	public User getPublisher() {
		return publisher;
	}

	public void setPublisher(User publisher) {
		this.publisher = publisher;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}







}
