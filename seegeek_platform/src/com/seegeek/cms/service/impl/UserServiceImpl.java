package com.seegeek.cms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seegeek.cms.action.Constance;
import com.seegeek.cms.dao.IDepartmentDao;
import com.seegeek.cms.dao.IUserDao;
import com.seegeek.cms.dao.IUserRoleDao;
import com.seegeek.cms.domain.Department;
import com.seegeek.cms.domain.User;
import com.seegeek.cms.domain.UserRole;
import com.seegeek.cms.service.IUserService;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:51:05 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, IUserDao> implements IUserService{
	@Autowired
	IUserRoleDao userRoleDao;
	public List<String> getListByUserId(String mybatis, Map<String, Object> map) {
		return genericDao.query(mybatis, map);
	}

	@Override
	public void update(String mybatisId, User entity) {
		
		//delete the reletion 
		userRoleDao.delete(Constance.DELETE_BYUSER_ID, entity.getId());
		List<UserRole> list=new ArrayList<UserRole>();
		UserRole u=null;
		if(entity.getRoleIds()!=null&&entity.getRoleIds().size()>0)
		{
			for(int i=0;i<entity.getRoleIds().size();i++)
			{
			u=new UserRole();
			u.setRoleId(entity.getRoleIds().get(i));
			u.setUserId(entity.getId());
			list.add(u);
			}
			
		}
		userRoleDao.insert("addUserRoles", list);
		super.update(mybatisId, entity);
		
		
	}

	@Override
	public void add(String mybatisId, User entity) {
		List<UserRole> list=new ArrayList<UserRole>();
		UserRole u=null;
		if(entity.getRoleIds()!=null&&entity.getRoleIds().size()>0)
		{
			for(int i=0;i<entity.getRoleIds().size();i++)
			{
			u=new UserRole();
			u.setRoleId(entity.getRoleIds().get(i));
			u.setUserId(entity.getId());
			list.add(u);
			}
			
		}
		userRoleDao.insert("addUserRoles",list);
		super.add(mybatisId, entity);
	}

	
}
