package com.seegeek.cms.service;

import java.util.List;
import java.util.Map;

import com.seegeek.cms.domain.User;

/**
 * @author  作者 zhaogaofei
 * @version  创建时间：May 12, 2015 12:17:58 PM
 * @email   zhaogaofei2012@163.com 
 * 类说明
 */
public interface IUserService extends IBaseService<User>{
 List<String> getListByUserId(String mybatis, Map<String, Object> map);
}
