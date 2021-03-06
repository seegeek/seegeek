package com.seegeek.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.seegeek.cms.dao.ILiveMediaDao;
import com.seegeek.cms.domain.LiveMedia;
import com.seegeek.cms.service.ILiveMediaService;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:51:05 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
@Service
public class LiveMediaServiceImpl extends BaseServiceImpl<LiveMedia, ILiveMediaDao> implements ILiveMediaService{


	public List<LiveMedia> getListByUserId(String mybatis, Map<String, Object> map) {
		return genericDao.query(mybatis, map);
	}
	public int getListByUserIdLiveMediaId(String mybatis,
			Map<String, Object> map) {
		return genericDao.queryCount(mybatis, map);
	}

}
