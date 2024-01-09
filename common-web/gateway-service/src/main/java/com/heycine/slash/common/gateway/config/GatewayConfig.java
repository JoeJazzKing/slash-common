package com.heycine.slash.common.gateway.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;

/**
 * 网关配置
 *
 * @author zzj
 */
@Configuration
public class GatewayConfig {

	private final List<ViewResolver> viewResolvers;

	public GatewayConfig(ObjectProvider<List<ViewResolver>> viewResolversProvider) {
		this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
	}
	
/*	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
		// Register the block exception handler for Spring Cloud Gateway.
		return new SentinelGatewayBlockExceptionHandler(viewResolvers, new DefaultServerCodecConfigurer());
	}*/
	
/*	@Bean
	@Order(-1)
	public GlobalFilter sentinelGatewayFilter() {

		return new SentinelGatewayFilter();
	}*/

/*	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SentinelFallbackHandler sentinelGatewayExceptionHandler() {

		return new SentinelFallbackHandler();
	}*/

	/**
	 * RPC配置
	 *
	 * @return
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {

		return new RestTemplate();
	}

}
