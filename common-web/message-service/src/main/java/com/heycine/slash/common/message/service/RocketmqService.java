package com.heycine.slash.common.message.service;

import com.heycine.slash.common.domain.dto.MqMessageDTO;

/**
 * 消息服务
 *
 * @author zzj
 */
public interface RocketmqService {

	/**
	 * 发送消息至MQ
	 * @param mqMessageDTO
	 * @return
	 */
	boolean sendMessageToMQ(MqMessageDTO<Object> mqMessageDTO);

}