package com.heycine.slash.common.gateway.util;

import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * @author zzj
 */
public class WebExchangeUtil {

	/**
	 * 获取请求路径
	 *
	 * @param request
	 * @return
	 */
	public static String gatePath(ServerHttpRequest request) {
		StringBuffer sb = new StringBuffer(request.getURI().getScheme());
		sb.append("://")
				.append(request.getURI().getHost()).append(":").append(request.getURI().getPort())
				.append(request.getURI().getPath());
		return sb.toString();
	}

}
