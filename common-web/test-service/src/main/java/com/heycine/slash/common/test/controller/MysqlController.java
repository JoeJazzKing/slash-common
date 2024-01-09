package com.heycine.slash.common.test.controller;

import com.heycine.slash.common.basic.http.R;
import com.heycine.slash.common.business.entity.TestOrderEntity;
import com.heycine.slash.common.business.repository.TestOrderRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mysql")
@Slf4j
@Api(tags = "mysql测试")
public class MysqlController {

    @Autowired
    private TestOrderRepository testOrderRepository;

    @GetMapping("/test")
    @ApiOperation(value = "连通性测试")
    public R<?> test() {
        List<TestOrderEntity> list = testOrderRepository.list();
        return R.ok(list);
    }

}
