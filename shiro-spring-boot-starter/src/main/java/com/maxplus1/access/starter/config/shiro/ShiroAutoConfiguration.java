package com.maxplus1.access.starter.config.shiro;

import com.maxplus1.access.starter.config.shiro.filter.AuthcFilter;
import com.maxplus1.access.starter.config.shiro.filter.PermsFilter;
import com.maxplus1.access.starter.config.shiro.interceptor.shiro.ShiroAdvisor;
import com.maxplus1.access.starter.config.shiro.realm.LoginRealm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qxloo on 2017/9/4.
 */
@Configuration
@EnableConfigurationProperties(ShiroProperties.class)
public class ShiroAutoConfiguration {

    @Autowired
    private ShiroProperties shiroProperties;

    @Bean
    public LoginRealm buildLoginRealm() {
        LoginRealm loginRealm = new LoginRealm();
        loginRealm.setAuthenticationCachingEnabled(false);
        return loginRealm;
    }

    @Bean
    public DefaultWebSessionManager buildDefaultWebSessionManager() {
        RestSessionManager defaultWebSessionManager = new RestSessionManager();
        defaultWebSessionManager.setGlobalSessionTimeout(shiroProperties.getGlobalSessionTimeout());
        defaultWebSessionManager.setSessionValidationInterval(shiroProperties.getSessionValidationInterval());
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);

//        defaultWebSessionManager.setCacheManager(); //默认 MapCache  SimpleSession  MemoryConstrainedCacheManager

        return defaultWebSessionManager;
    }




    @Bean
    public DefaultWebSecurityManager buildDefaultWebSecurityManager(@Autowired LoginRealm loginRealm, @Autowired DefaultWebSessionManager defaultWebSessionManager) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager(loginRealm);
        defaultWebSecurityManager.setSessionManager(defaultWebSessionManager);
        return defaultWebSecurityManager;
    }

    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilter(@Autowired DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
       /*
       // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");

        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/core/unauthorized");
        */
        if (shiroProperties.getTestMode()) {
            shiroProperties.getFilterChainDefinitionMap().put("/**", "anon");
        }
        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroProperties.getFilterChainDefinitionMap());

        //设置filters
        Map<String,Filter> filterMap = new HashMap<>();
        filterMap.put("authc",new AuthcFilter());
        filterMap.put("perms",new PermsFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        return shiroFilterFactoryBean;
    }



    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }


    /**
     * 测试模式下自动关闭@RequirePermissions之类的注解
     * @param securityManager
     * @return
     */
    @Bean
    @ConditionalOnProperty
            (
                    value = "spring.maxplus1.shiro.testMode",
                    havingValue = "false",
                    matchIfMissing = true
            )
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Autowired DefaultWebSecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new ShiroAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}

