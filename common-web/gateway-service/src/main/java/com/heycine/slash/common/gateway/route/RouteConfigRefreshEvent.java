package com.heycine.slash.common.gateway.route;

import lombok.Data;

/**
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 *
 * @author zhiji.zhou
 * @date 2022/5/17 下午4:37
 */
@Data
public class RouteConfigRefreshEvent {

	private String fileContent;

	public RouteConfigRefreshEvent(String fileContent) {

		this.fileContent = fileContent;
	}

}
