package com.seegeek.cms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.seegeek.cms.action.Constance;
import com.seegeek.cms.dao.IRoleResourceDao;
import com.seegeek.cms.domain.RoleResource;
import com.seegeek.cms.service.IRoleResourceService;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:51:05 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
@Service
public class RoleResourceServiceImpl extends BaseServiceImpl<RoleResource, IRoleResourceDao> implements IRoleResourceService{

	@Override
	public void add(String mybatisId, List<RoleResource> entity) {
		//取出第一个roleId,做删除任务，然后再执行批量插入
		RoleResource roleResource=entity.get(0);
		this.delete(Constance.DELETE_BYROLE_ID, roleResource.getRoleId());
		super.add(mybatisId, entity);
	}

}
