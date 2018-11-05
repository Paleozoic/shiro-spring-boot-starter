package com.maxplus1.access.starter.config.shiro;

import com.maxplus1.access.starter.config.shiro.cache.RedisSessionDAO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

@Slf4j
@EnableConfigurationProperties({RedisProperties.class,ShiroProperties.class})
@ConditionalOnProperty
(
        value = "spring.maxplus1.shiro.redisCacheEnabled",
        havingValue = "true"
)
//@Configuration
public class SetSessionDAOAutoConfiguration {

    @Autowired
    private ShiroProperties shiroProperties;
    @Autowired
    private DefaultWebSessionManager defaultWebSessionManager;
    @Autowired
    private RedisSessionDAO redisSessionDAO;

    @PostConstruct
    public DefaultWebSessionManager setSessionDao(){
        /**
         * 默认Session放在内存：MemorySessionDAO
         */
        if(shiroProperties.getRedisCacheEnabled()){
            defaultWebSessionManager.setSessionDAO(redisSessionDAO);
        }
        return defaultWebSessionManager;
    }
}
