package com.heycine.slash.common.activity.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ActivitySecurityAdapter {

    @Autowired
    @Qualifier("customUserDetailsService")
    private UserDetailsService userDetailsService;

    public void logInAs(String userKey) {
        // 用户不存在则手动条件，既等于跳过用户验证机制
        this.addUser(userKey);

        // 加载用户
        UserDetails user = userDetailsService.loadUserByUsername(userKey);
        if (null == user) {
            throw new IllegalStateException("User " + userKey + " doesn't exist, please provide a valid user");
        }

        SecurityContextHolder.setContext(new SecurityContextImpl(new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return user.getAuthorities();
            }
            @Override
            public Object getCredentials() {
                return user.getPassword();
            }
            @Override
            public Object getDetails() {
                return user;
            }
            @Override
            public Object getPrincipal() {
                return user;
            }
            @Override
            public boolean isAuthenticated() {
                return true;
            }
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }
            @Override
            public String getName() {
                return user.getUsername();
            }
        }));
        org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(userKey);
    }

    /**
     * 添加用户
     *
     * @param userKey
     */
    public void addUser(String userKey) {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = ((InMemoryUserDetailsManager) userDetailsService);
        if (inMemoryUserDetailsManager.userExists(userKey)) {
            return;
        }

        String pwd = "password";
        String[] roleConfig = { "ROLE_ACTIVITI_USER", "GROUP_DEFAULT" };
        inMemoryUserDetailsManager.createUser(new User(userKey, pwd,
                Arrays.stream(roleConfig).map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
    }

}
