package com.seegeek.cms.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * 短信存储
 * @author 作者 zhaogaofei
 * @email 邮箱 zhaogaofei2012@163.com
 * @date 创建时间 Dec 2, 2015 4:01:09 PM
 */
public class MessageStorage {

	private RedisTemplate<String, String> redisTemplate;

	public void addOrUpdate(String key,String message) {
		redisTemplate.opsForValue().set("user.message."+key, message);
		redisTemplate.expire("user.message."+key, 120, TimeUnit.SECONDS);
	}

	public String load(String userId) {
		return redisTemplate.opsForValue().get("user.message." + userId);
	}

	public void delete(String userId) {
		redisTemplate.delete("user.message." + userId);
	}

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}