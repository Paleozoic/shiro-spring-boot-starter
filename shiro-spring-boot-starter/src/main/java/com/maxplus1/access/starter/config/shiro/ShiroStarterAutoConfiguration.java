package com.maxplus1.access.starter.config.shiro;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Import({com.maxplus1.access.starter.config.shiro.ShiroRedisAutoConfiguration.class,
        com.maxplus1.access.starter.config.shiro.SetSessionDAOAutoConfiguration.class,
        com.maxplus1.access.starter.config.shiro.ShiroAutoConfiguration.class,
        com.maxplus1.access.starter.config.shiro.WebMvcAutoConfiguration.class})
@Configuration
@AutoConfigureAfter(name = {"com.maxplus1.db.starter.config.MyBatisStarterAutoConfiguration"})
public class ShiroStarterAutoConfiguration {
}
