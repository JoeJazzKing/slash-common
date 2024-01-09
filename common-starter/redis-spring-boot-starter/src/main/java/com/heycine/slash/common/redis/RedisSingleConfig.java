//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.heycine.slash.common.redis;

import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author zzj
 */
@ConditionalOnProperty(
		prefix = "spring.redis",
		name = {"type"},
		havingValue = "single"
)
@Slf4j
public class RedisSingleConfig extends AbstractRedisConfig {

	@Autowired
	private RedisProperties redisProperties;

	public RedisSingleConfig() {
	}

	@Override
	@Bean({"redissonClient"})
	public RedissonClient redissonClient() {
		log.info("init redis address:{}", this.redisProperties.getHost());
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + this.redisProperties.getHost() + ":" + this.redisProperties.getPort());
		StringBuilder password = new StringBuilder();
		if (StringUtil.isNotEmpty(this.redisProperties.getUsername())) {
			password.append(this.redisProperties.getUsername());
			password.append(":");
		}

		if (StringUtil.isNotEmpty(this.redisProperties.getPassword())) {
			password.append(this.redisProperties.getPassword());
		}

		if (StringUtil.isNotBlank(password.toString())) {
			config.useSingleServer().setPassword(password.toString());
		}

		return Redisson.create(config);
	}

	@Override
	@Bean
	@Primary
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
				this.redisProperties.getHost(),
				this.redisProperties.getPort()
		);
		StringBuilder password = new StringBuilder();
		if (StringUtil.isNotEmpty(this.redisProperties.getUsername())) {
			password.append(this.redisProperties.getUsername());
			password.append(":");
		}

		if (StringUtil.isNotEmpty(this.redisProperties.getPassword())) {
			password.append(this.redisProperties.getPassword());
		}

		if (StringUtil.isNotBlank(password.toString())) {
			redisStandaloneConfiguration.setPassword(password.toString());
		}

		return new LettuceConnectionFactory(redisStandaloneConfiguration, this.getLettuceClientConfiguration());
	}

}
