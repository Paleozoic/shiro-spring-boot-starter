package com.maxplus1.access.starter.config.shiro;

import com.maxplus1.access.starter.config.shiro.cache.RedisSessionDAO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@EnableConfigurationProperties({RedisProperties.class,ShiroProperties.class})
@ConditionalOnProperty
(
        value = "spring.maxplus1.shiro.redisCacheEnabled",
        havingValue = "true"
)
//@Configuration
public class ShiroRedisAutoConfiguration {

    /**
     * RedisAutoConfiguration加载默认RedisTemplate的Condition是：
     * @ConditionalOnMissingBean(name = "redisTemplate")
     * 这里这里需要给Bean起一个别名。
     * @param redisConnectionFactory
     * @return
     */
    @Bean("shiroRedisTemplate")
    public RedisTemplate<String,Object> redisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }



    @Bean
    public RedisSessionDAO redisSessionDAO(@Autowired @Qualifier(value = "shiroRedisTemplate") RedisTemplate redisTemplate){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisTemplate(( RedisTemplate<String,Session>)redisTemplate);
        return redisSessionDAO;
    }
}
