package com.maxplus1.access.starter.config.shiro;

import com.maxplus1.access.starter.config.shiro.interceptor.LoginInterceptor;
import com.maxplus1.access.starter.config.shiro.interceptor.RequestResourceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by qxloo on 2017/9/5.
 */
@Configuration
@EnableConfigurationProperties(ShiroProperties.class)
public class WebMvcAutoConfiguration implements WebMvcConfigurer {


    @Autowired
    private ShiroProperties shiroProperties;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //web资源拦截器，通过此拦截器将request,response等存进ThreadLocal。
        registry.addInterceptor(new RequestResourceInterceptor()).addPathPatterns("/**");
        //登陆拦截器，存储一些账号信息进session
        registry.addInterceptor(loginInterceptor).addPathPatterns(shiroProperties.getLoginUrl());
        //权限拦截器
//        registry.addInterceptor(accessInterceptor).addPathPatterns("/**")/*.excludePathPatterns()*/;
    }




}
