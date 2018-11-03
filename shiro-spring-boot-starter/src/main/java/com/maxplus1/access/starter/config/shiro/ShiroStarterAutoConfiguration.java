package com.maxplus1.access.starter.config.shiro;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Import({com.maxplus1.access.starter.config.shiro.ShiroRedisAutoConfiguration.class,
        com.maxplus1.access.starter.config.shiro.SetSessionDAOAutoConfiguration.class,
        com.maxplus1.access.starter.config.shiro.ShiroAutoConfiguration.class,
        com.maxplus1.access.starter.config.shiro.WebMvcAutoConfiguration.class})
@Configuration
public class ShiroStarterAutoConfiguration {
}
