package com.seegeek.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.seegeek.cms.dao.IUserDao;
import com.seegeek.cms.domain.User;
import com.seegeek.cms.service.IUserService;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:51:05 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, IUserDao> implements IUserService{
	public List<String> getListByUserId(String mybatis, Map<String, Object> map) {
		return genericDao.query(mybatis, map);
	}
}
