package com.heycine.slash.common.gateway.filter;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.heycine.slash.common.basic.http.R;
import com.heycine.slash.common.gateway.config.constant.CacheConstants;
import com.heycine.slash.common.gateway.util.ObjectUtils;
import com.heycine.slash.common.gateway.util.WebExchangeUtil;
import com.heycine.slash.common.gateway.config.properties.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 网关认证
 *
 * @author Alikes
 */
@Slf4j
@RefreshScope
@Component
public class AuthFilter implements GlobalFilter, Ordered {

	private final String BEARER_TOKEN_TYPE = "Bearer ";

	private final String AUTH_TYPE_KEY = "auth_type";

	private final String AUTH_TYPE_MEMBER = "member";

	private final String AUTH_TYPE_PASSWORD = "password";

	private final String AUTH_TYPE_DINGTALK = "dingTalk";

	private static final String GATEWAY_CLIENT_AUTHORIZATION = "Basic "
			+ Base64.getEncoder().encodeToString("gateway-service:123456".getBytes());


	private static final String MEMBER_CLIENT_AUTHORIZATION = "Basic "
			+ Base64.getEncoder().encodeToString("member-client:123456".getBytes());

	@Resource
	private IgnoreWhiteProperties ignoreWhite;

//	@Resource
//	private RemoteAuthService remoteAuthService;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String url = exchange.getRequest().getURI().getPath();
		// 跳过不需要验证的路径
		List<String> whites = ignoreWhite.getWhites();
		if (ObjectUtils.matches(url, whites)) {
			// 登录请求,特殊处理一下,接入oauth认证,通过auth_type区分不同来源
			if (exchange.getRequest().getURI().toString().contains("athena/login")
					|| exchange.getRequest().getURI().toString().contains("auth/oauth/login")) {
				// 网关身份
				ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
				String method = request.getMethodValue();
				if ("POST".equals(method)) {
					return DataBufferUtils.join(exchange.getRequest().getBody())
							.flatMap(dataBuffer -> {
								byte[] bytes = new byte[dataBuffer.readableByteCount()];
								dataBuffer.read(bytes);
								try {
									String bodyString = new String(bytes, "utf-8");
									Map<String, String> paramMap = getParam(bodyString);
									log.info(bodyString);//打印请求参数
									String authType = paramMap.get(AUTH_TYPE_KEY);
									if (AUTH_TYPE_MEMBER.equalsIgnoreCase(authType)) {
										builder.header("Authorization", MEMBER_CLIENT_AUTHORIZATION);
									} else if (AUTH_TYPE_PASSWORD.equalsIgnoreCase(authType) || AUTH_TYPE_DINGTALK.equalsIgnoreCase(authType)) {
										builder.header("Authorization", GATEWAY_CLIENT_AUTHORIZATION);
									} else if (StringUtils.isBlank(authType) && StringUtils.isBlank(paramMap.get("client_id")) && StringUtils.isBlank(paramMap.get("client_secret"))) {
										builder.header("Authorization", GATEWAY_CLIENT_AUTHORIZATION);
									}
									exchange.getAttributes().put("POST_BODY", bodyString);
								} catch (UnsupportedEncodingException e) {
									log.error(e.getMessage(), e);
								}
								DataBufferUtils.release(dataBuffer);
								Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
									DataBuffer buffer = exchange.getResponse().bufferFactory()
											.wrap(bytes);
									return Mono.just(buffer);
								});

								ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
									@Override
									public Flux<DataBuffer> getBody() {
										return cachedFlux;
									}
								};
								return chain.filter(exchange.mutate().request(mutatedRequest.mutate().build()).build());
							});
				}
				return chain.filter(exchange.mutate().request(builder.build()).build());
			}
			return chain.filter(exchange);
		} else {
			String token = getToken(exchange.getRequest());
			log.debug(token);
			if (StringUtils.isBlank(token)) {
				return setUnauthorizedResponse(exchange, "请登录后访问!");
			}
			try {
				/*if (remoteAuthService == null) {
					remoteAuthService = null; *//*SpringUtil.getBean(RemoteAuthService.class)*//*
				}*/
				// TODO 远程Auth验证
				R<Map<String, ?>> checkTokenRes = null;/*remoteAuthService.checkToken(token.substring(BEARER_TOKEN_TYPE.length()))*/
				;
				log.info(JSONUtil.toJsonStr(checkTokenRes));
				if (!checkTokenRes.isOk()) {
					return setUnauthorizedResponse(exchange, "请登录后访问!");
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			String userId = exchange.getRequest().getHeaders().getFirst(CacheConstants.DETAILS_USER_ID);
			String userName = exchange.getRequest().getHeaders().getFirst(CacheConstants.DETAILS_USERNAME);
			// 设置用户信息到请求
			ServerHttpRequest mutableReq = exchange.getRequest().mutate()
					.header(CacheConstants.AUTHORIZATION_HEADER, token).header(CacheConstants.DETAILS_USER_ID, userId)
					.header(CacheConstants.DETAILS_USERNAME, userName).build();
			ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
			return chain.filter(mutableExchange);
		}
	}

	@Bean
	@ConditionalOnMissingBean
	public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
		return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
	}

	/**
	 * 未登录
	 *
	 * @param exchange
	 * @param msg
	 * @return
	 */
	private Mono<Void> setUnauthorizedResponse(ServerWebExchange exchange, String msg) {
		ServerHttpResponse response = exchange.getResponse();
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		response.setStatusCode(HttpStatus.OK);
		log.warn("[鉴权异常处理]请求路径:{}", WebExchangeUtil.gatePath(exchange.getRequest()));
		return response.writeWith(Mono.fromSupplier(() -> {
			DataBufferFactory bufferFactory = response.bufferFactory();
			return bufferFactory.wrap(JSON.toJSONBytes(R.unauthorized(new HashMap<>()).path(gatePath(exchange.getRequest()))));
		}));
	}

	/**
	 * 获取请求token
	 */
	private String getToken(ServerHttpRequest request) {
		String token = request.getHeaders().getFirst(CacheConstants.TOKEN_HEADER);
		return token;
	}

	@Override
	public int getOrder() {
		return -200;
	}

	/**
	 * 获取请求路径
	 *
	 * @param request
	 * @return
	 */
	private String gatePath(ServerHttpRequest request) {
		StringBuffer sb = new StringBuffer(request.getURI().getScheme());
		sb.append("://")
				.append(request.getURI().getHost()).append(":").append(request.getURI().getPort())
				.append(request.getURI().getPath());
		return sb.toString();
	}


	/**
	 * 解析出url参数中的键值对
	 * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
	 *
	 * @param strUrlParam url地址
	 * @return url请求参数部分
	 * @author lzf
	 */
	public Map<String, String> getParam(String strUrlParam) {
		Map<String, String> mapRequest = new HashMap<String, String>();
		String[] arrSplit = null;
		if (strUrlParam == null) {
			return mapRequest;
		}
		arrSplit = strUrlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");
			//解析出键值
			if (arrSplitEqual.length > 1) {
				//正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
			} else {
				if (arrSplitEqual[0] != "") {
					//只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}

}
