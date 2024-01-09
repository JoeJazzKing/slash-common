package com.heycine.slash.common.redis.service;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisService {

	public RedisTemplate redisTemplate;

	public RedisService(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public <T> void setCacheObject(String key, T value) {

		this.redisTemplate.opsForValue().set(key, value);
	}

	public <T> void setCacheObject(String key, T value, Long timeout, TimeUnit timeUnit) {

		this.redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
	}

	public boolean expire(String key, long timeout) {

		return this.expire(key, timeout, TimeUnit.SECONDS);
	}

	public Boolean expire(String key, long timeout, TimeUnit unit) {

		return this.redisTemplate.expire(key, timeout, unit);
	}

	public <T> T getCacheObject(String key) {
		ValueOperations<String, T> operation = this.redisTemplate.opsForValue();
		return operation.get(key);
	}

	public Boolean deleteObject(String key) {

		return this.redisTemplate.delete(key);
	}

	public Long deleteObject(Collection collection) {

		return this.redisTemplate.delete(collection);
	}

	public <T> Long setCacheList(String key, List<T> dataList) {
		Long count = this.redisTemplate.opsForList().rightPushAll(key, dataList);
		return count == null ? 0L : count;
	}

	public <T> List<T> getCacheList(String key) {

		return this.redisTemplate.opsForList().range(key, 0L, -1L);
	}

	public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet) {
		BoundSetOperations<String, T> setOperation = this.redisTemplate.boundSetOps(key);
		Iterator it = dataSet.iterator();

		while (it.hasNext()) {
			setOperation.add((T) new Object[]{it.next()});
		}

		return setOperation;
	}

	public <T> Set<T> getCacheSet(String key) {

		return this.redisTemplate.opsForSet().members(key);
	}

	public <T> void setCacheMap(String key, Map<String, T> dataMap) {
		if (dataMap != null) {
			this.redisTemplate.opsForHash().putAll(key, dataMap);
		}
	}

	public <T> Map<String, T> getCacheMap(String key) {
		return this.redisTemplate.opsForHash().entries(key);
	}

	public <T> void setCacheMapValue(String key, String hKey, T value) {

		this.redisTemplate.opsForHash().put(key, hKey, value);
	}

	public <T> T getCacheMapValue(String key, String hKey) {
		HashOperations<String, String, T> opsForHash = this.redisTemplate.opsForHash();
		return opsForHash.get(key, hKey);
	}

	public <T> List<T> getMultiCacheMapValue(String key, Collection<Object> hKeys) {

		return this.redisTemplate.opsForHash().multiGet(key, hKeys);
	}

	public Collection<String> keys(String pattern) {

		return this.redisTemplate.keys(pattern);
	}

}
