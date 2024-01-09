package com.heycine.slash.common.gateway.route;

import cn.hutool.core.map.MapUtil;
import com.heycine.slash.common.gateway.config.properties.IgnoreWhiteProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理动态刷新路由配置
 * <p>
 * ::优雅编程，此刻做起！
 * ::Elegant programming, start now!
 *
 * @author zhiji.zhou
 * @date 2022/5/17 下午4:32
 */
@Component
@Slf4j
@RefreshScope
public class FileRouteDefinitionHandler implements RouteDefinitionRepository, ApplicationEventPublisherAware {
	@Resource
	private IgnoreWhiteProperties ignoreWhite;

	private ApplicationEventPublisher publisher;

	private List<RouteDefinition> routeDefinitionList = new ArrayList<>();

	private String yamlFileContent;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {

		this.publisher = publisher;
	}

	@PostConstruct
	public void init() {

		load();
	}

	/**
	 * 监听事件刷新配置
	 */
	@EventListener
	public void listenEvent(RouteConfigRefreshEvent event) {
		this.yamlFileContent = event.getFileContent();
		load();

		try {
			// 路由刷新
			this.publisher.publishEvent(new RefreshRoutesEvent(this));
			// 白名单刷新
			ignoreWhite.refreshWhitesArray();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.info(ex.getMessage());
		}
	}

	/**
	 * 刷新白名单
	 *
	 * @return void
	 * @author zhiji.zhou
	 * @date 2022.05.17 18:27:41
	 */
	private void reflushIgnoreWhite() {
//		securityWebFilterChain = securityConfig.springSecurityFilterChain(security);
	}

	/**
	 * 加载
	 */
	private void load() {
		if (StringUtils.isBlank(yamlFileContent)) {
			return;
		}

		try {
			Yaml yaml = new Yaml();
			Map routesMap = yaml.load(yamlFileContent);
			Map spring = MapUtil.get(routesMap, "spring", Map.class);
			Map cloud = MapUtil.get(spring, "cloud", Map.class);
			Map gateway = MapUtil.get(cloud, "gateway", Map.class);
			List<HashMap<String, Object>> routes = MapUtil.get(gateway, "routes", List.class);

			for (HashMap<String, Object> route : routes) {
				RouteDefinition routeDefinition = new RouteDefinition();
				routeDefinition.setId(MapUtil.getStr(route, "id"));
				routeDefinition.setUri(new URI(MapUtil.getStr(route, "uri")));
				routeDefinition.setPredicates(MapUtil.get(route, "predicates", List.class));
				routeDefinition.setFilters(MapUtil.get(route, "filters", List.class));

				routeDefinitionList.add(routeDefinition);
			}

			log.info("路由配置已加载,加载条数:{}", routeDefinitionList.size());
		} catch (Exception e) {
			log.error("从文件加载路由配置异常", e);
		}
	}

	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {

		return Flux.fromIterable(routeDefinitionList);
	}

	/**
	 * 新增
	 * 不支持新增操作
	 *
	 * @param route
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 * @author zhiji.zhou
	 * @date 2022.05.17 16:35:01
	 */
	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return Mono.defer(() -> Mono.error(new NotFoundException("Unsupported operation")));
	}

	/**
	 * 修改
	 * 不支持修改操作
	 *
	 * @param routeId
	 * @return reactor.core.publisher.Mono<java.lang.Void>
	 * @author zhiji.zhou
	 * @date 2022.05.17 16:35:33
	 */
	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		return Mono.defer(() -> Mono.error(new NotFoundException("Unsupported operation")));
	}

}
