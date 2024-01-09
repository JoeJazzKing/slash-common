package com.heycine.slash.common.test.controller;

import com.heycine.slash.common.basic.http.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
@Api(tags = "rocketmq测试")
public class RocketMqController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public final static  String TOPIC = "TEST";
    public final static String GROUP = "TEST_GROUP";

    @GetMapping("/send")
    @ApiOperation(value = "发送消息")
    public R<?> test(@RequestParam String msg) {
        SendResult sendResult = rocketMQTemplate.syncSend(TOPIC, msg);
        return R.ok(sendResult);
    }

}

