package com.heycine.slash.common.socket.socket.dto;

import lombok.Data;

/**
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 *
 * @author zhiji.zhou
 * @date 2022/4/26 下午4:18
 */
@Data
public class SocketMsgDTO {

	/**
	 * 通知消息
	 */
	public static final String NOTIFY_MESSAGE = "200";

	/**
	 * 通信数据
	 */
	public static final String TRANSFER_DATA = "202";

	/**
	 * 日志消息
	 */
	public static final String LOG_DATA = "203";

	/**
	 * 登录挤退
	 */
	public static final String LOGIN_EXIT = "300";


	/**
	 * 状态码
	 */
	private String code;
	/**
	 * 消息说明
	 */
	private String msg;
	/**
	 * 消息内容
	 */
	private String content;

	public SocketMsgDTO() {
		this.code = NOTIFY_MESSAGE;
	}

	public SocketMsgDTO(String code) {
		this.code = code;
	}

}



