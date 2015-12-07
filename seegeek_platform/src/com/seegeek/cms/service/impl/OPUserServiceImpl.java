package com.seegeek.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seegeek.cms.action.Constance;
import com.seegeek.cms.dao.IOPUserDao;
import com.seegeek.cms.dao.IUserRoleDao;
import com.seegeek.cms.domain.OPUser;
import com.seegeek.cms.domain.UserRole;
import com.seegeek.cms.service.IOPUserService;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:51:05 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
@Service
public class OPUserServiceImpl extends BaseServiceImpl<OPUser, IOPUserDao> implements IOPUserService{

	@Autowired
	public IUserRoleDao iUserRoleDao;

	@Override
	public void add(String mybatisId, OPUser entity) {
		super.add(mybatisId, entity);
		UserRole u=null;
		if(entity.getRoleIds()!=null&&entity.getRoleIds().size()>0)
		{
			for(int i=0;i<entity.getRoleIds().size();i++)
			{
			u=new UserRole();
			u.setRoleId(entity.getRoleIds().get(i));
			u.setUserId(entity.getId());
			iUserRoleDao.insert(Constance.ADD_OBJECT, u);
			}
			
		}
	}
	public void update(String mybatisId, OPUser entity) {
		super.update(mybatisId, entity);
		iUserRoleDao.delete(Constance.DELETE_BYUSER_ID, entity.getId());
		UserRole u=null;
		if(entity.getRoleIds()!=null&&entity.getRoleIds().size()>0)
		{
			for(int i=0;i<entity.getRoleIds().size();i++)
			{
			u=new UserRole();
			u.setRoleId(entity.getRoleIds().get(i));
			u.setUserId(entity.getId());
			iUserRoleDao.insert(Constance.ADD_OBJECT, u);
			}
			
		}
	}
	
	
}
