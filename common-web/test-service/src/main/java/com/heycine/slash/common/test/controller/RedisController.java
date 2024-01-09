package com.heycine.slash.common.test.controller;

import com.heycine.slash.common.basic.http.R;
import com.heycine.slash.common.business.entity.TestOrderEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/redis")
@Api(tags = "redis测试")
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/set")
    @ApiOperation(value = "写入值")
    public R<?> test(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForValue().set(key, value);
        return R.ok();
    }

    @GetMapping("/get")
    @ApiOperation(value = "读取值")
    public R<?> test(@RequestParam String key) {
        Object o = redisTemplate.opsForValue().get(key);
        return R.ok(o);
    }
}
