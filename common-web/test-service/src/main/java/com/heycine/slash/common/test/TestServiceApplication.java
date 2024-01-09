package com.heycine.slash.common.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 监控中心
 *
 * @author Alikes
 */

@SpringBootApplication(scanBasePackages = {"com.heycine.slash"})
@MapperScan(basePackages = {"com.heycine.slash.common.business.mapper"})
@EntityScan(basePackages = {"com.heycine.slash.common.business.entity"})

@EnableDiscoveryClient
@Slf4j
public class TestServiceApplication {

	public static void main(String[] args) {
		TimeInterval timer = DateUtil.timer();

		SpringApplication.run(TestServiceApplication.class, args);

		log.info("=========== test-service服务启动成功 =========== 共耗时：{} ms", timer.interval());
	}

}
