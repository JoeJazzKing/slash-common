package com.heycine.slash.common.socket.socket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 *
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 * @author zhiji.zhou
 * @date 2022/4/25 下午6:12
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter () {

        return new ServerEndpointExporter();
    }

}