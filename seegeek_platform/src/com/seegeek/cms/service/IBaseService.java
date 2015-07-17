package com.seegeek.cms.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 12, 2015 12:18:03 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public interface IBaseService<T> {
	void add(String mybatisId,T entity);
	void add(String mybatisId,Map<String,Object> map);
	void update(String mybatisId,T entity);
	void delete(String mybatisId,Serializable id);
	T get(String mybatisId,Serializable id);
	T get(String mybatisId,T entity);
	List<T> getAll(String mybatisId);
	List<T> getList(String mybatisId,Object param);
	List<T> getList(String mybatisId,Map<String,String> param);
    int queryCount(String _mybitsId, Map<String, Object> _params); 
}
