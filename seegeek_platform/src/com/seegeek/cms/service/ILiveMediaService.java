package com.seegeek.cms.service;

import java.util.List;
import java.util.Map;

import com.seegeek.cms.domain.LiveMedia;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 12, 2015 12:17:53 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public interface ILiveMediaService extends IBaseService<LiveMedia>{
	List<LiveMedia> getListByUserId(String getListByuserid, Map<String,Object> maps);
	int getListByUserIdLiveMediaId(String getListByuserid, Map<String,Object> map);
}
