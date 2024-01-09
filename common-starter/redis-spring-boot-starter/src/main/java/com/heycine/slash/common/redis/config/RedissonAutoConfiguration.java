package com.heycine.slash.common.redis.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 *
 * @author zhiji.zhou
 * @date 2022/9/23 下午3:08
 */
@EnableConfigurationProperties(RedissonProperties.class)
@Configuration
public class RedissonAutoConfiguration {

	@Bean
	TestReidsClient testReidsClient(RedissonProperties redissonProperties) {
		// ...测试业务
		return new TestReidsClient();
	}

}
