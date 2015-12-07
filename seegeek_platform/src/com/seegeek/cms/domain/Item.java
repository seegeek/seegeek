package com.seegeek.cms.domain;

public class Item {
	/**
	 * 主键字段
	 */
	private Integer id;
	//点播
	private LiveMedia liveMedia; 
	//直播
	private OfflineMedia offlineMedia;
	//播放类型
	private Integer PlayType;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LiveMedia getLiveMedia() {
		return liveMedia;
	}
	public void setLiveMedia(LiveMedia liveMedia) {
		this.liveMedia = liveMedia;
	}
	public OfflineMedia getOfflineMedia() {
		return offlineMedia;
	}
	public void setOfflineMedia(OfflineMedia offlineMedia) {
		this.offlineMedia = offlineMedia;
	}
	public Integer getPlayType() {
		return PlayType;
	}
	public void setPlayType(Integer playType) {
		PlayType = playType;
	}
	
	
}
