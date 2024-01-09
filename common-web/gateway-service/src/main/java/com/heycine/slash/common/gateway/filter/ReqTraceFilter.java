package com.heycine.slash.common.gateway.filter;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class ReqTraceFilter implements GlobalFilter, GatewayFilter, Ordered {

	/**
	 * httpheader，traceId的key名称
	 */
	private static final String TRACE_ID = "traceId";

	private static final String CONTENT_TYPE = "Content-Type";

	private static final String CONTENT_TYPE_JSON = "application/json";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		MDC.put(TRACE_ID, IdUtil.fastSimpleUUID());
		ServerHttpRequest request = exchange.getRequest();
		// 判断过滤器是否执行
		String bodyStr = "";
		String contentType = request.getHeaders().getFirst(CONTENT_TYPE);
		String method = request.getMethodValue();
		// 判断是否为POST请求
		if (null != contentType && HttpMethod.POST.name().equalsIgnoreCase(method) && contentType.contains(CONTENT_TYPE_JSON)) {
			ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
			MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
			List<String> list = new ArrayList<>();
			// 读取请求体
			Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
				// 记录请求体日志
				final String nId = saveRequestOperaLog(exchange, body);
				// 记录日志id
				list.add(MDC.get(TRACE_ID));
				return Mono.just(body);
			});

			BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
			HttpHeaders headers = new HttpHeaders();
			headers.putAll(exchange.getRequest().getHeaders());
			headers.remove(HttpHeaders.CONTENT_LENGTH);
			CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
			return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
				ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
					@Override
					public HttpHeaders getHeaders() {
						long contentLength = headers.getContentLength();
						HttpHeaders httpHeaders = new HttpHeaders();
						httpHeaders.putAll(super.getHeaders());
						httpHeaders.put(TRACE_ID, list);
						if (contentLength > 0) {
							httpHeaders.setContentLength(contentLength);
						} else {
							httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
						}
						return httpHeaders;
					}

					@Override
					public Flux<DataBuffer> getBody() {
						return outputMessage.getBody();
					}
				};

				return chain.filter(exchange.mutate().request(decorator).build());
			}));
		}
		if (HttpMethod.GET.name().equalsIgnoreCase(method)) {
			bodyStr = request.getQueryParams().toString();
			String nId = saveRequestOperaLog(exchange, bodyStr);
			ServerHttpRequest userInfo = exchange.getRequest().mutate().header(TRACE_ID, nId).build();
			return chain.filter(exchange.mutate().request(userInfo).build());
		}
		return chain.filter(exchange);
	}

	/**
	 * 保存请求日志
	 *
	 * @param exchange
	 * @param requestParameters
	 * @return
	 */
	private String saveRequestOperaLog(ServerWebExchange exchange, String requestParameters) {
		log.info("接口请求参数：{}", requestParameters);
		ServerHttpRequest request = exchange.getRequest();
		String ip = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
		log.info("当前请求ip:{}", ip);
		return IdUtil.fastSimpleUUID();
	}

	@Override
	public int getOrder() {
		return 5;
	}
}
