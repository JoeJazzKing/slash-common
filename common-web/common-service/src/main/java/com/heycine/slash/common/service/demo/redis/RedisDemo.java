package com.heycine.slash.common.service.demo.redis;

import com.heycine.slash.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RedisDemo {

    @Autowired(required = false)
    RedisService redisService;

    @GetMapping("redis/send/{id}")
    public String send(@PathVariable("id") String id){
        redisService.setCacheObject("TEST:1", "我是小周周" + id);
        return "SUCESS";
    }

    @GetMapping("redis/find")
    public String find(){
        Object cacheObject = redisService.getCacheObject("TEST:1");
        return cacheObject.toString();
    }
}
