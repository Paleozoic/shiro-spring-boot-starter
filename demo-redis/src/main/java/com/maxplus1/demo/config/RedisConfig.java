package com.maxplus1.demo.config;

import com.maxplus1.access.starter.config.shiro.cache.RedisSessionDAO;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean("redisTemplate")
    public RedisTemplate<String,Object> redisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(keySerializer());
        redisTemplate.setHashKeySerializer(keySerializer());
        redisTemplate.setValueSerializer(valueSerializer());
        redisTemplate.setHashValueSerializer(valueSerializer());

        return redisTemplate;
    }

    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }


    @Bean
    public RedisSessionDAO redisSessionDAO(@Autowired @Qualifier(value = "redisTemplate") RedisTemplate redisTemplate){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisTemplate(( RedisTemplate<String,Session>)redisTemplate);
        return redisSessionDAO;
    }
}
