package com.maxplus1.access.starter.config.shiro;

import com.maxplus1.access.starter.config.shiro.bean.App;
import com.maxplus1.access.starter.config.shiro.rbac.ShiroUser;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;

@Data
@ConfigurationProperties("spring.maxplus1.shiro")
public class ShiroProperties {

    private App app;

    private String tokenKey;
    private String loginUrl;
    private String filterChain;
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
    private ShiroUser mockUser;


    @PostConstruct
    public void buildFilterChainDefinitionMap() {
        String filterChain = this.getFilterChain();
        if(filterChain!=null && filterChain.length()>0){
            LinkedHashMap<String, String> map =new LinkedHashMap<>();
            String[] filterChainArr = filterChain.split("\n");
            for (String s : filterChainArr) {
                String[] split = s.split("=");
                map.put(split[0],split[1]);
            }
            this.setFilterChainDefinitionMap(map);
        }
    }
}
