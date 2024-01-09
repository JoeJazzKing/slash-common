package com.heycine.slash.common.monitor.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
@Component
@Slf4j
public class WebSocketClientS1 {

	@Value("${websocket.server.url:127.0.0.1:33004/websocket}")
	private String serverUrl;

	@Value("${websocket.server.user:system@1}")
	private String user;

	private Session session;

	@PostConstruct
	void init() {
		try {
			// 本机地址
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			String wsUrl = "ws://" + serverUrl + "/" + user;
			URI uri = URI.create(wsUrl);
			session = container.connectToServer(WebSocketClientS1.class, uri);
		} catch (DeploymentException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开连接
	 * @param session
	 */
	@OnOpen
	public void onOpen(Session session) {
		this.session = session;

		log.info("打开socket链接");
	}

	/**
	 * 关闭连接
	 */
	@OnClose
	public void onClosing() throws IOException {
		if(this.session.isOpen()){
			this.session.close();
		}

		log.info("关闭socket链接");
	}

	/**
	 * 接收消息
	 * @param text
	 */
	@OnMessage
	public void onMessage(String text) {
		log.info("接受到socket消息：{}", text);
	}

	/**
	 * 异常处理
	 * @param throwable
	 */
	@OnError
	public void onError(Throwable throwable) {
		throwable.printStackTrace();
		log.info("socket发生异常：{}", throwable.getMessage());
	}

	/**
	 * 主动发送消息
	 */
	public void send(String message) {
		this.session.getAsyncRemote().sendText(message);
		log.info("推送消息到socket：{}", message);
	}

}
