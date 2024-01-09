package com.heycine.slash.common.message.service.impl;

import com.heycine.slash.common.domain.dto.MqMessageDTO;
import com.heycine.slash.common.message.service.RocketmqService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * 消息服务
 *
 * @author zzj
 */
@Service
public class RocketmqServiceImpl implements RocketmqService {

	@Autowired
	RocketMQTemplate rocketMQTemplate;

	@Override
	public boolean sendMessageToMQ(MqMessageDTO<Object> mqMessageDTO) {
		try {
			rocketMQTemplate.send(
					// topic主题
					mqMessageDTO.getTopic(),
					// content消息内容
					MessageBuilder.withPayload(mqMessageDTO.getContent()).build()
			);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		return true;
	}

}