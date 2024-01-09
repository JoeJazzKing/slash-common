package com.heycine.slash.common.socket;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 监控中心
 *
 * @author Alikes
 */
@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class SocketServiceApplication {

	public static void main(String[] args) {
		TimeInterval timer = DateUtil.timer();

		SpringApplication.run(SocketServiceApplication.class, args);

		log.info("=========== socket-service服务启动成功 =========== 共耗时：{} ms", timer.interval());
	}

}
