package com.heycine.slash.common.message.controller;

import com.heycine.slash.common.basic.http.R;
import com.heycine.slash.common.domain.dto.MqMessageDTO;
import com.heycine.slash.common.message.service.RocketmqService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息服务
 *
 * @author zzj
 */
@RestController
@RequestMapping("/rocketmq")
@Api(tags = "消息服务")
public class RocketmqController {

	@Autowired
	private RocketmqService rocketmqService;

	@PostMapping("/send")
	@ApiOperation(value = "发送消息至MQ")
	public R<?> sendMessageToMQ(@RequestBody MqMessageDTO<Object> mqMessageDTO) {
		boolean isSuccess = rocketmqService.sendMessageToMQ(mqMessageDTO);
		if (isSuccess) {
			return R.ok();
		} else {
			return R.fail();
		}
	}

}