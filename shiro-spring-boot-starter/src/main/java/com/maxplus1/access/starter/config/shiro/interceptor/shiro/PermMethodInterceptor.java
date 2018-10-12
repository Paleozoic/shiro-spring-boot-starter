package com.maxplus1.access.starter.config.shiro.interceptor.shiro;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;

/**
 * 自定义注解的方法拦截器
 */
public class PermMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {


    /*
     * The character to look for that closes a permission definition.
     **/
    //private static final char ARRAY_CLOSE_CHAR = ']';

    /**
     * Default no-argument constructor that ensures this interceptor looks for
     * {@link org.apache.shiro.authz.annotation.RequiresPermissions RequiresPermissions} annotations in a method declaration.
     */
    public PermMethodInterceptor() {
        super( new PermAnnotationHandler() );
    }

    /**
     * @param resolver
     * @since 1.1
     */
    public PermMethodInterceptor(AnnotationResolver resolver) {
        super( new PermAnnotationHandler(), resolver);
    }
}