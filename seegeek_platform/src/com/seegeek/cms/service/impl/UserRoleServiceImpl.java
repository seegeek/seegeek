package com.seegeek.cms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.seegeek.cms.action.Constance;
import com.seegeek.cms.dao.IUserRoleDao;
import com.seegeek.cms.domain.RoleResource;
import com.seegeek.cms.domain.UserRole;
import com.seegeek.cms.service.IUserRoleService;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:51:05 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole, IUserRoleDao> implements IUserRoleService{

	@Override
	public void add(String mybatisId, List<UserRole> entity) {
		//取出第一个roleId,做删除任务，然后再执行批量插入
		UserRole userRole=entity.get(0);
		this.delete(Constance.DELETE_BYUSER_ID, userRole.getUserId());
		super.add(mybatisId, entity);
	}

}
