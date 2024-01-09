package com.heycine.slash.common.message.properties;

import cn.hutool.core.util.ArrayUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送消息队列
 *
 * @author zzj
 */
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "rocketmq")
@Data
public class RocketmqProperties {

}
