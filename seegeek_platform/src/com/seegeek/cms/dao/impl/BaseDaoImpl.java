package com.seegeek.cms.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;

import com.seegeek.cms.dao.IBaseDao;
import com.seegeek.cms.utils.GeneriacUtils;
import com.seegeek.cms.utils.PageBean;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:50:21 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public abstract class BaseDaoImpl<T> implements IBaseDao<T> {
	
	@Resource
	private SqlSessionTemplate sqlSession;

	public <T> int delete(String _mybitsId, T obj) {
		return sqlSession.delete(getDefaultSqlNamespace()+"."+_mybitsId, obj);
	}

	private String getDefaultSqlNamespace() {
		Class<T> clz = GeneriacUtils.getSuperClassGenericType(getClass());
		return clz.getName();
	}

	public <T> int insert(String _mybitsId, T obj) {
		return sqlSession.insert(getDefaultSqlNamespace()+"."+_mybitsId, obj);
	}
	public <T> int insert(String _mybitsId, List<T> obj) {
		return sqlSession.insert(getDefaultSqlNamespace()+"."+_mybitsId, obj);
	}
	public <T> int insertMap(String _mybitsId, List<Map<String, String>> obj) {
		return sqlSession.insert(getDefaultSqlNamespace()+"."+_mybitsId, obj);
	}

	public <T> int update(String _mybitsId, T obj) {
		return sqlSession.update(getDefaultSqlNamespace()+"."+_mybitsId, obj);
	}

	public <T> List<T> query(String _mybitsId, Map<String, Object> _params) {
		
		return sqlSession.selectList(getDefaultSqlNamespace()+"."+_mybitsId, _params);
	}
	public int queryCount(String _mybitsId, Map<String, Object> _params) {
		return sqlSession.selectOne(getDefaultSqlNamespace()+"."+_mybitsId, _params);
	}

	public <T> List<T> query(String _mybitsId, Object _params) {
		return sqlSession.selectList(getDefaultSqlNamespace()+"."+_mybitsId, _params);
	}

	public T queryOne(String _mybitsId, Object object) {
		return sqlSession.selectOne(getDefaultSqlNamespace()+"."+_mybitsId, object);
	}

	public void setSqlSession(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}

	public <T> int insert(String id, Map<String, Object> map) {
		return sqlSession.insert(getDefaultSqlNamespace()+"."+id, map);
	}

	@SuppressWarnings("unchecked")
	public PageBean queryPage(String _mybitsId, Map<String, Object> map) {
		PageBean bean=new PageBean(map.get("startRow")==null?1:Integer.valueOf(map.get("startRow").toString()),map.get("pageNum")==null?1:Integer.valueOf(map.get("pageNum").toString()),map.get("pageSize")==null?1:Integer.valueOf(map.get("pageSize").toString()));
		//mysql start recordIndex
		map.put("startRow", bean.getStartRow());
		map.put("pageSize", bean.getPageSize());
		System.out.println("<<<<startRow>>>>>--"+bean.getStartRow());
		System.out.println("<<<<endRow>>>>--"+bean.getEndRow());
		List<T> list=sqlSession.selectList(getDefaultSqlNamespace()+"."+_mybitsId, map);
		int totalNum=sqlSession.selectOne(getDefaultSqlNamespace()+".totalNum",map);
		bean.setTotalRows(totalNum);
		bean.setResultList(list);
		return bean;
	}
	
}
