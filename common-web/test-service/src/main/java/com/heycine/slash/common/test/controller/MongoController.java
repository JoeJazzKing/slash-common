package com.heycine.slash.common.test.controller;

import com.heycine.slash.common.basic.http.R;
import com.heycine.slash.common.business.entity.TestOrderEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mongo")
@Slf4j
@Api(tags = "mongo测试")
public class MongoController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/insert")
    @ApiOperation(value = "插入")
    public R<?> insert() {
        MongoOrder mongoOrder = new MongoOrder();
        mongoOrder.setId(3L);
        mongoOrder.setInfo("测试订单");

        mongoTemplate.insert(mongoOrder);
        return R.ok();
    }

    @GetMapping("/find")
    @ApiOperation(value = "查找")
    public R<?> find() {

        MongoOrder order = mongoTemplate.findOne(new Query(Criteria.where("info").is("测试订单")), MongoOrder.class);
        return R.ok(order);
    }

}
