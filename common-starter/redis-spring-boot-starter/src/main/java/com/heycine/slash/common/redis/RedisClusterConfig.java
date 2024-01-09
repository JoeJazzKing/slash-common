package com.heycine.slash.common.redis;

import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.List;

/**
 * @author zzj
 */
@ConditionalOnProperty(
		prefix = "spring.redis",
		name = {"type"},
		havingValue = "cluster",
		matchIfMissing = true
)
@Slf4j
public class RedisClusterConfig extends AbstractRedisConfig {

	@Autowired
	private RedisProperties redisProperties;

	public RedisClusterConfig() {
	}

	@Override
	@Bean({"redissonClient"})
	public RedissonClient redissonClient() {
		List<String> nodes = this.redisProperties.getCluster().getNodes();
		log.info("init redis nodes:{}", nodes);
		Config config = new Config();

		for (int i = 0; i < nodes.size(); ++i) {
			config.useClusterServers().setScanInterval(3000).addNodeAddress("redis://" + nodes.get(i));
		}

		if (StringUtil.isNotEmpty(this.redisProperties.getPassword())) {
			config.useClusterServers().setPassword(this.redisProperties.getPassword());
		}

		if (StringUtil.isNotEmpty(this.redisProperties.getUsername())) {
			config.useClusterServers().setUsername(this.redisProperties.getUsername());
		}

		return Redisson.create(config);
	}

	@Override
	@Bean
	@Primary
	public RedisConnectionFactory redisConnectionFactory() {
		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(this.redisProperties.getCluster().getNodes());
		if (StringUtil.isNotEmpty(this.redisProperties.getPassword())) {
			redisClusterConfiguration.setPassword(this.redisProperties.getPassword());
		}

		if (StringUtil.isNotEmpty(this.redisProperties.getUsername())) {
			redisClusterConfiguration.setUsername(this.redisProperties.getUsername());
		}

		return new LettuceConnectionFactory(redisClusterConfiguration, this.getLettuceClientConfiguration());
	}

}
