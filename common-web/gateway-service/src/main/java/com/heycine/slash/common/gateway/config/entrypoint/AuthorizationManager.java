package com.heycine.slash.common.gateway.config.entrypoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 *
 * @author zzj
 */
@Slf4j
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        // 只负责认证,不负责鉴权,鉴权由服务自己控制
        return mono
                .filter(Authentication::isAuthenticated)
                .map((authentication) -> new AuthorizationDecision(authentication.isAuthenticated()))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}
