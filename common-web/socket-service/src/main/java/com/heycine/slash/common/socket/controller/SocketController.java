package com.heycine.slash.common.socket.controller;

import com.heycine.slash.common.socket.socket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 多端登录验证，如果已经登录，则需要挤退
 */
@RestController
@RequestMapping("/socket")
@Api(tags = "Socket服务管理")
public class SocketController {

	/**
	 * 清空在线人数
	 *
	 * @return java.lang.String
	 * @author zhiji.zhou
	 * @date 2022.04.26 15:16:13
	 */
	@GetMapping("/clear")
	@ApiOperation(value = "清空在线人数")
	public String clear() {
		int onlineCount = WebSocketServer.getOnlineCount();

		WebSocketServer.clear();

		return "清理在线人数：" + onlineCount + "人";
	}

	/***
	 * 管理员群发
	 * @param message
	 * @return java.lang.String
	 *
	 * @author zhiji.zhou
	 * @date 2022.04.26 17:10:08
	 */
	@PostMapping("/sendGroupMsg")
	@ApiOperation(value = "管理员群发")
	public String sendGroupMsg(@RequestBody String message) {
		int onlineCount = WebSocketServer.getOnlineCount();

		WebSocketServer.sendInfo(message, null);

		return "群发消息成功，共发送：" + onlineCount + "人";
	}

	/***
	 * 管理员单发
	 * @param message
	 * @return java.lang.String
	 *
	 * @author zhiji.zhou
	 * @date 2022.04.26 17:10:08
	 */
	@PostMapping("/sendPersonMsg")
	@ApiOperation(value = "管理员单发")
	public String sendPersonMsg(@RequestParam String message, @RequestParam String userName) {
		boolean b = WebSocketServer.sendInfo(message, userName);
		if (!b) {
			return "单发消息失败！发送人：" + userName + ", 没有在线或不存在！";
		}

		return "单发消息成功！发送人：" + userName;
	}

}
