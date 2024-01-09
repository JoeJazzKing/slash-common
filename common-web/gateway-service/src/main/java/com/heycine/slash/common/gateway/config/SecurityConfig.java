package com.heycine.slash.common.gateway.config;


import com.heycine.slash.common.gateway.config.entrypoint.AuthorizationManager;
import com.heycine.slash.common.gateway.config.entrypoint.RestAuthenticationEntryPoint;
import com.heycine.slash.common.gateway.config.entrypoint.RestfulAccessDeniedHandler;
import com.heycine.slash.common.gateway.config.constant.GatewayConstant;
import com.heycine.slash.common.gateway.filter.RemoveJwtFilter;
import com.heycine.slash.common.gateway.config.properties.IgnoreWhiteProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 安全配置
 *
 * @author zzj
 */
@AllArgsConstructor
@Configuration
@Component
@EnableWebFluxSecurity
public class SecurityConfig {

	private final AuthorizationManager authorizationManager;
	private IgnoreWhiteProperties ignoreWhite;
	private final RestfulAccessDeniedHandler restfulAccessDeniedHandler;
	private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	private final RemoveJwtFilter ignoreUrlsRemoveJwtFilter;

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http.csrf().disable();
		http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
		// 自定义处理JWT请求头过期或签名错误的结果
		http.oauth2ResourceServer().authenticationEntryPoint(restAuthenticationEntryPoint);
		// 对白名单路径，直接移除JWT请求头
		http.addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
		http.authorizeExchange()
				// 白名单配置，使用filter过滤练，此处全部放开
				.pathMatchers(ignoreWhite.getWhitesArray()).permitAll()
				// 鉴权管理器配置
				.anyExchange().access(authorizationManager).and().exceptionHandling()
				// 处理未授权
				.accessDeniedHandler(restfulAccessDeniedHandler)
				// 处理未认证
				.authenticationEntryPoint(restAuthenticationEntryPoint).and().csrf().disable();
		return http.build();
	}

	@Bean
	public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix(GatewayConstant.AUTHORITY_PREFIX);
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(GatewayConstant.AUTHORITY_CLAIM_NAME);

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

		return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
	}

}
