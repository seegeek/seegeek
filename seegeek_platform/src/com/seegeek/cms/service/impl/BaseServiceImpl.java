package com.seegeek.cms.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.seegeek.cms.dao.IBaseDao;
import com.seegeek.cms.service.IBaseService;
import com.seegeek.cms.utils.GeneriacUtils;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:51:15 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public abstract class BaseServiceImpl<T,E extends IBaseDao<T>> implements IBaseService<T>{

	protected Class<T> clz=GeneriacUtils.getSuperClassGenericType(getClass());

	protected E genericDao;
	public void add(String mybatisId, T entity) {
		genericDao.insert(mybatisId, entity);
	}
	public void delete(String mybatisId, Serializable id) {
		genericDao.delete(mybatisId, id);
	}
	public T get(String mybatisId, Serializable id) {
		return genericDao.queryOne(mybatisId, id);
	}
	public List<T> getAll(String mybatisId) {
	return 	genericDao.query(mybatisId, "");
	}
	public void update(String mybatisId, T entity) {
		
	}
	public E getGenericDao() {
		return genericDao;
	}
	@Autowired
	public void setGenericDao(E genericDao) {
		this.genericDao = genericDao;
	}


}
