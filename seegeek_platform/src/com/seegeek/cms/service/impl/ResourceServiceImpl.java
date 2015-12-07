package com.seegeek.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.seegeek.cms.dao.IResourceDao;
import com.seegeek.cms.domain.Resource;
import com.seegeek.cms.service.IResourceService;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:51:05 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
@Service
public class ResourceServiceImpl extends BaseServiceImpl<Resource, IResourceDao> implements IResourceService{

	public List<String> getStringList(String mybatis, Map<String, Object> map) {
		return genericDao.query(mybatis, map);
	}
}
