package com.maxplus1.access.starter.config.shiro.interceptor.shiro;
import org.apache.shiro.spring.aop.SpringAnnotationResolver;
import org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor;

/**
 * 自定义注解的AOP拦截器
 */
public class PermAopInterceptor extends AopAllianceAnnotationsAuthorizingMethodInterceptor {
    public PermAopInterceptor() {
        super();
        // 添加自定义的注解拦截器
        this.methodInterceptors.add(new PermMethodInterceptor(new SpringAnnotationResolver()));
    }
}
