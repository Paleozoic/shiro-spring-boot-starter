package com.maxplus1.access.starter.config.shiro;

import com.maxplus1.access.starter.config.shiro.rbac.User;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;

@Data
@ConfigurationProperties("spring.maxplus1.shiro")
public class ShiroProperties {
    private String tokenKey;
    private String loginUrl;
    private LinkedHashMap<String, String> filterChainDefinitionMap;

    /**
     * shiro
     */
    private Long globalSessionTimeout;
    private Long sessionValidationInterval;

    /**
     * 是否开启Redis分布式Session
     */
    private Boolean redisCacheEnabled = true;

    /**
     * 测试模式，所有URL的权限都是anon（允许匿名访问）
     */
    private Boolean testMode = false;
    private User mockUser;

}
