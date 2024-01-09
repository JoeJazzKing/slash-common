package com.heycine.slash.common.test.controller;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
        topic = RocketMqController.TOPIC,
        consumerGroup = RocketMqController.GROUP,
        messageModel = MessageModel.BROADCASTING
)
public class RocketMqConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("消费者接受到的消息:" + s);
    }
}