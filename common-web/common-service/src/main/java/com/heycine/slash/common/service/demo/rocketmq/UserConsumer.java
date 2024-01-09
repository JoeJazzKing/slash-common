package com.heycine.slash.common.service.demo.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "JoeTest", consumerGroup = "JoeTest-GROUP")
public class UserConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        log.warn("接受到消息: {}", message);
    }

}
