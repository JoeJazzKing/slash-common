package com.heycine.slash.common.gateway.route;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 *
 * @author zhiji.zhou
 * @date 2022/5/17 下午3:58
 */
@Component
@Slf4j
public class NacosConfigReflushListener implements ApplicationEventPublisherAware {
	private ApplicationEventPublisher publisher;

	@Autowired
	private ApplicationContext applicationContext;

	@Value("${spring.cloud.nacos.config.prefix}")
	private String dataId;
	@Value("${spring.cloud.nacos.config.file-extension}")
	private String format;
	@Value("${spring.cloud.nacos.config.group}")
	private String group;

	@Autowired
	private void addReceiveConfigListener(NacosConfigProperties nacosConfigProperties) throws NacosException {
		ConfigService configService = nacosConfigProperties.configServiceInstance();
		String id = dataId + "." + format;
		configService.addListener(id, group,
				new AbstractListener() {
					@Override
					public void receiveConfigInfo(String configInfo) {
						log.info("监听到Nacos配置文件发生改变！");

						// 推送更新内存路由的事件
						NacosConfigReflushListener bean = applicationContext.getBean(NacosConfigReflushListener.class);
						bean.publisher.publishEvent(new RouteConfigRefreshEvent(configInfo));
					}
				});
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {

		this.publisher = publisher;
	}

}
