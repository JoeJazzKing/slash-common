package com.heycine.slash.common.monitor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
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
@EnableAdminServer
@Slf4j
public class MonitorServiceApplication {

    public static void main(String[] args) {
        TimeInterval timer = DateUtil.timer();

        SpringApplication.run(MonitorServiceApplication.class, args);

        log.info("=========== monitor-service服务启动成功 =========== 共耗时：{} ms", timer.interval());
    }
    
}
