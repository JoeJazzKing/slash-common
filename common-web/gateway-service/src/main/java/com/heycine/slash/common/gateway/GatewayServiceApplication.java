package com.heycine.slash.common.gateway;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.FilterType;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * springBoot 启动类
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 *
 * @author zhiji.zhou
 * @date 2022/8/25 下午9:58
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScans(value = {
		@ComponentScan(
				basePackages = {"com.heycine.slash.common"},
				excludeFilters = @ComponentScan.Filter(
						type = FilterType.REGEX,
						pattern = "com.heycine.slash.common.basic.handler.RestControllerExceptionHandler"
				)
		)
})
@EnableSwagger2
@EnableDiscoveryClient
public class GatewayServiceApplication {

	public static void main(String[] args) {
		TimeInterval timer = DateUtil.timer();

		SpringApplication.run(GatewayServiceApplication.class, args);

		log.info("=========== gateway-service服务启动成功 =========== 共耗时：{} ms", timer.interval());
	}

}
