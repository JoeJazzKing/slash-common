package com.heycine.slash.common.socket.socket;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.heycine.slash.common.socket.socket.dto.SocketMsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 * 客户端连接地址格式 --> ws://127.0.0.1:33004/websocket/userName
 * @author zhiji.zhou
 * @date 2022/4/25 下午6:12
 */
@ServerEndpoint("/websocket/{sid}")
@Component
@Slf4j
public class WebSocketServer {
	/**
	 * 用来存放每个客户端对应的WebSocket对象。
	 */
	private static final CopyOnWriteArraySet<WebSocketServer> WEB_SOCKET_SET = new CopyOnWriteArraySet<>();
	/**
	 * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	 */
	private static int onlineCount = 0;
	/**
	 * 与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	private Session session;
	/**
	 * 接收sid
	 */
	private String sid = "";

	/**
	 * 连接建立成功调用的方法
	 *
	 * @param session
	 * @param sid
	 * @return void
	 * @author zhiji.zhou
	 * @date 2022.04.25 18:22:52
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("sid") String sid) {
		// 如果已经登录，则发送挤退信息
		WebSocketServer wss = WebSocketServer.getWebSocket(sid);
		if (wss != null) {
			wss.sendMessageToExit();
		}

		this.sid = sid;
		this.session = session;

		//加入set中
		WEB_SOCKET_SET.add(this);
		//在线数加1
		addOnlineCount();
		log.info("有新窗口开始监听:{},当前在线人数为{}", sid, getOnlineCount());

		try {
			SocketMsgDTO socketMsgDTO = new SocketMsgDTO(SocketMsgDTO.LOG_DATA);
			socketMsgDTO.setMsg("连接成功！");
			sendMessage(socketMsgDTO);
		} catch (IOException e) {
			log.error("websocket IO异常");
		}
	}

	/**
	 * 连接关闭调用的方法
	 *
	 * @return void
	 * @author zhiji.zhou
	 * @date 2022.04.25 18:22:58
	 */
	@OnClose
	public void onClose() {
		//从set中删除
		WEB_SOCKET_SET.remove(this);
		//在线数减1
		subOnlineCount();
		log.info("有1个连接关闭！当前在线人数为{}", getOnlineCount());

	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message
	 * @param session
	 * @return void
	 * @author zhiji.zhou
	 * @date 2022.04.25 18:23:46
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("收到来自窗口【{}】的信息: {}", sid, message);
		//群发消息
		SocketMsgDTO socketMsgDTO = new SocketMsgDTO();
		socketMsgDTO.setMsg(message);

		for (WebSocketServer item : WEB_SOCKET_SET) {
			try {
				item.sendMessage(socketMsgDTO);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 连接异常时
	 *
	 * @param session
	 * @param error
	 * @return void
	 * @author zhiji.zhou
	 * @date 2022.04.25 18:23:56
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.info("Socket连接发生异常，原因：{}", error.getMessage());
		error.printStackTrace();
	}

	/**
	 * 获取在线人数
	 *
	 * @return int
	 * @author zhiji.zhou
	 * @date 2022.04.25 18:15:46
	 */
	public static synchronized int getOnlineCount() {

		return onlineCount;
	}

	/**
	 * 添加1个在线人数
	 *
	 * @return void
	 * @author zhiji.zhou
	 * @date 2022.04.25 18:16:00
	 */
	public static synchronized void addOnlineCount() {

		WebSocketServer.onlineCount++;
	}

	/**
	 * 删除一个在线人数
	 *
	 * @return void
	 * @author zhiji.zhou
	 * @date 2022.04.25 18:16:21
	 */
	public static synchronized void subOnlineCount() {

		WebSocketServer.onlineCount--;
		if (WebSocketServer.onlineCount < 0) {
			WebSocketServer.onlineCount = 0;
		}
	}

	/**
	 * 清空在线人数
	 *
	 * @return void
	 * @author zhiji.zhou
	 * @date 2022.04.26 15:18:03
	 */
	public static void clear() {
		int onlineCount = getOnlineCount();
		WEB_SOCKET_SET.clear();
		WebSocketServer.onlineCount = 0;
		log.info("清空在线人数成功！共：{}人", onlineCount);
	}

	/**
	 * 获取socket链接
	 *
	 * @param sid
	 * @return com.anji.captcha.demo.socket.WebSocketServer
	 * @author zhiji.zhou
	 * @date 2022.04.25 18:17:10
	 */
	public static WebSocketServer getWebSocket(String sid) {
		if (WEB_SOCKET_SET.size() <= 0) {
			return null;
		}

		for (WebSocketServer item : WEB_SOCKET_SET) {
			if (StrUtil.equals(sid, item.sid)) {
				return item;
			}
		}

		return null;
	}

	/**
	 * 群发/单发自定义消息
	 */
	public static boolean sendInfo(String message, @PathParam("sid") String sid) {
		log.info("推送消息到窗口: {}，推送内容: {}", sid, message);

		SocketMsgDTO socketMsgDTO = new SocketMsgDTO();
		socketMsgDTO.setMsg(message);
		boolean flag = false;

		// 群发
		if (StrUtil.isEmpty(sid)) {
			try {
				for (WebSocketServer item : WEB_SOCKET_SET) {
					item.sendMessage(socketMsgDTO);
				}
				flag = true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		// 单发
		} else {
			try {
				for (WebSocketServer item : WEB_SOCKET_SET) {
					if (StrUtil.equals(sid, item.sid)) {
						item.sendMessage(socketMsgDTO);
						flag = true;
						break;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return flag;
	}

	/**
	 * 实现服务器主动推送
	 */
	private void sendMessage(SocketMsgDTO socketMsgDTO) throws IOException {
		String content = JSONObject.toJSONString(socketMsgDTO);
		this.session.getBasicRemote().sendText(content);

		log.info("Socket主动推送了消息：{}", content);
	}

	/**
	 * 发送挤退消息
	 *
	 * @return void
	 * @author zhiji.zhou
	 * @date 2022.04.25 18:59:20
	 */
	private void sendMessageToExit() {
		try {
			SocketMsgDTO socketMsgDTO = new SocketMsgDTO(SocketMsgDTO.LOGIN_EXIT);
			socketMsgDTO.setMsg("被登录挤退！");
			this.sendMessage(socketMsgDTO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
