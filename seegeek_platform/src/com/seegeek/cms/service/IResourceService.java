package com.seegeek.cms.service;

import java.util.List;
import java.util.Map;

import com.seegeek.cms.domain.Resource;


/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 12, 2015 12:17:53 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public interface IResourceService extends IBaseService<Resource>{
	 List<String> getStringList(String mybatis,Map<String, Object> map);
}
