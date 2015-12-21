package com.seegeek.cms.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.seegeek.cms.dao.IBaseDao;
import com.seegeek.cms.service.IBaseService;
import com.seegeek.cms.utils.GeneriacUtils;
import com.seegeek.cms.utils.PageBean;

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
	public void add(String mybatisId, List<T> entity) {
		genericDao.insert(mybatisId, entity);
	}
	public void delete(String mybatisId, Serializable id) {
		genericDao.delete(mybatisId, id);
	}
	public T get(String mybatisId, Serializable id) {
		return genericDao.queryOne(mybatisId, id);
	}
	public T get(String mybatisId,   T entity) {
		return genericDao.queryOne(mybatisId, entity);
	}
	public List<T> getAll(String mybatisId) {
	Map<String, String> map =new HashMap<String, String>();
	map.put("startRow", null);
	return 	genericDao.query(mybatisId,map);
	}
	public void update(String mybatisId, T entity) {
		genericDao.update(mybatisId, entity);
	}
	public void update(String mybatisId, Map<String, Object> map) {
		genericDao.update(mybatisId, map);
	}
	
	public List<T> getList(String mybatisId, Object param) {
		return 	genericDao.query(mybatisId,param);
	}
	
	
	public List<T> getList(String mybatisId, Map<String, String> param) {
		return 	genericDao.query(mybatisId,param);
	}
	
	
	public E getGenericDao() {
		return genericDao;
	}
	@Autowired
	public void setGenericDao(E genericDao) {
		this.genericDao = genericDao;
	}
	public void add(String mybatisId, Map<String, Object> map) {
		 	genericDao.insert(mybatisId, map);
	}
	public int queryCount(String id, Map<String, Object> _params) {
		// TODO Auto-generated method stub
		return 	genericDao.queryCount(id, _params);
	}
	public List<T> getList(String mybatisId, List<String> list) {
		return 	genericDao.query(mybatisId,list);
	}
	public PageBean queryPage(String id, Map<String, Object> map) {
		return 	genericDao.queryPage(id, map);
	}


}
