package com.heycine.slash.common.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 *
 * @author zhiji.zhou
 * @date 2022/9/23 下午2:58
 */
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonProperties {
	private String host;

	private int port;

	private int timeout;

	private boolean ssl;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}
}
