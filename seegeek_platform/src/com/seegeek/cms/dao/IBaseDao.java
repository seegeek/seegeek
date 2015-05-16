package com.seegeek.cms.dao;

import java.util.List;
import java.util.Map;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 11, 2015 2:50:08 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public interface IBaseDao <T>{

 public  <T>  int insert(String _mybitsId,  T obj);

 public  <T>  int update(String _mybitsId, T obj);

 public  <T>  int delete(String _mybitsId, T obj);
  

 public  <T>  List<T> query(String _mybitsId, Map<String, Object> _params);

 public <T> List<T> query(String _mybitsId, Object _params);

 public T queryOne(String queryString, Object object);

}

 

 
