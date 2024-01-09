package com.heycine.slash.common.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.heycine.slash.common.redis.service.RedisService;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.time.Duration;

/**
 * @author zzj
 */
public abstract class AbstractRedisConfig {
	@Value("${spring.cache.redis.key-prefix:COMMON:}")
	private String cacheKeyPrefix;

	public AbstractRedisConfig() {
	}

	public abstract RedisConnectionFactory redisConnectionFactory();

	public abstract RedissonClient redissonClient();

	public LettuceClientConfiguration getLettuceClientConfiguration() {
		ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions
				.builder()
				.enableAllAdaptiveRefreshTriggers()
				.adaptiveRefreshTriggersTimeout(Duration.ofSeconds(10L))
				.dynamicRefreshSources(false)
				.build();
		ClusterClientOptions clusterClientOptions = ClusterClientOptions
				.builder()
				.timeoutOptions(TimeoutOptions.enabled(Duration.ofSeconds(30L)))
				.autoReconnect(false)
				.pingBeforeActivateConnection(Boolean.TRUE)
				.cancelCommandsOnReconnectFailure(Boolean.TRUE)
				.topologyRefreshOptions(clusterTopologyRefreshOptions)
				.build();
		LettuceClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration
				.builder()
				.poolConfig(new GenericObjectPoolConfig())
				.clientOptions(clusterClientOptions)
				.build();
		return lettuceClientConfiguration;
	}

	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		return container;
	}

	@Bean({"redisService"})
	public RedisService redisService(RedisTemplate redisTemplate) {

		return new RedisService(redisTemplate);
	}

	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, DefaultTyping.NON_FINAL, As.PROPERTY);
		serializer.setObjectMapper(mapper);

		RedisTemplate<Object, Object> template = new RedisTemplate();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new KeyStringRedisSerializer(this.cacheKeyPrefix));
		template.setValueSerializer(serializer);
		template.setHashKeySerializer(new KeyStringRedisSerializer(this.cacheKeyPrefix));
		template.setHashValueSerializer(serializer);
		template.afterPropertiesSet();
		return template;
	}

}
