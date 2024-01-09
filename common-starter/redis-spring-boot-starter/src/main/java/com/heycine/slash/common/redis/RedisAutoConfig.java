package com.heycine.slash.common.redis;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

/**
 * @author zzj
 */
@Configuration
@EnableCaching
@Import({RedisClusterConfig.class, RedisSingleConfig.class})
public class RedisAutoConfig extends CachingConfigurerSupport {

	public RedisAutoConfig() {
	}

	@Override
	@Bean
	public KeyGenerator keyGenerator() {
		return (target, method, objects) ->
				new StringBuilder()
				.append(target.getClass().getName())
				.append(".").append(method.getName())
				.append(Arrays.toString(objects))
				.toString();
	}

}
