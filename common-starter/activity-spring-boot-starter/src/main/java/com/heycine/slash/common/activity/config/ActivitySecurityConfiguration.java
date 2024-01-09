package com.heycine.slash.common.activity.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class ActivitySecurityConfiguration {

    // [Test] 这里添加用户，后面处理流程时用到的任务负责人，需要添加在这里
    public final static String ONE_AUDIT = "GROUP_oneAudit";
    public final static String TWO_AUDIT = "GROUP_twoAudit";
    public final static String THREE_AUDIT = "GROUP_threeAudit";

    // 用户id，用户密码，用户角色，用户分组
    public static String[][] usersGroupsAndRoles = {
            { "system", "password", "ROLE_ACTIVITI_USER" },
            { "admin", "password", "ROLE_ACTIVITI_ADMIN" }
    };

    @Bean
    public UserDetailsService customUserDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

        for (String[] user : usersGroupsAndRoles) {
            List<String> authoritiesStrings = Arrays.asList(Arrays.copyOfRange(user, 2, user.length));
            log.info("> Registering new user: " + user[0] + " with the following Authorities " + authoritiesStrings);

            inMemoryUserDetailsManager.createUser(
                    new User(user[0], passwordEncoder().encode(user[1]),
                    authoritiesStrings.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
            );
        }
        return inMemoryUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

}
