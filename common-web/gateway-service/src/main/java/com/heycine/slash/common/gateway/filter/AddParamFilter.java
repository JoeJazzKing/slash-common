package com.heycine.slash.common.gateway.filter;

import com.heycine.slash.common.gateway.util.IpUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author zzj
 */
@Deprecated
@Component
public class AddParamFilter implements GatewayFilter, Ordered {

	@Override
	public int getOrder() {

		return 0;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		URI uri = exchange.getRequest().getURI();
		StringBuilder query = new StringBuilder();
		String originalQuery = uri.getRawQuery();

		if (StringUtils.hasText(originalQuery)) {
			query.append(originalQuery);
			if (originalQuery.charAt(originalQuery.length() - 1) != '&') {
				query.append('&');
			}
		}
		String remoteHost = /*"0.0.0.0"*/   IpUtils.getIpAddr(exchange.getRequest());
		query.append("ip").append('=').append(remoteHost);
		try {
			URI newUri = UriComponentsBuilder
					.fromUri(uri)
					.replaceQuery(query.toString())
					.build(true)
					.toUri();
			ServerHttpRequest request = exchange
					.getRequest()
					.mutate()
					.uri(newUri)
					.build();
			return chain.filter(exchange.mutate().request(request).build());
		} catch (RuntimeException ex) {
			throw new IllegalStateException("Invalid URI query: \"" + query + "\"");
		}
	}

}
