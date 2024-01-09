package com.heycine.slash.common.service.demo.rocketmq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RocketMqDemo {

    @Autowired(required = false)
    RocketMQTemplate rocketMQTemplate;

    @GetMapping("send/{id}")
    public String send(@PathVariable("id") String id){
        String name  = "我是小周周" + id;
        log.warn(JSON.toJSONString(name));
        rocketMQTemplate.send("JoeTest", MessageBuilder.withPayload(name).build());
        return "SUCESS";
    }

}
