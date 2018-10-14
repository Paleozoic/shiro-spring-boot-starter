package com.maxplus1.access.starter.config.shiro.interceptor.shiro;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;

/**
 * 方法拦截器
 */
@Deprecated
public class PermMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {



    public PermMethodInterceptor( PermAnnotationHandler handler ) {
        super(handler);
    }

    /**
     *
     * @param handler
     * @param resolver
     */
    public PermMethodInterceptor( PermAnnotationHandler handler,
                                                   AnnotationResolver resolver) {
        super(handler, resolver);
    }

    /**
     * Ensures the <code>methodInvocation</code> is allowed to execute first before proceeding by calling the
     * {@link #assertAuthorized(org.apache.shiro.aop.MethodInvocation) assertAuthorized} method first.
     *
     * @param methodInvocation the method invocation to check for authorization prior to allowing it to proceed/execute.
     * @return the return value from the method invocation (the value of {@link org.apache.shiro.aop.MethodInvocation#proceed() MethodInvocation.proceed()}).
     * @throws org.apache.shiro.authz.AuthorizationException if the <code>MethodInvocation</code> is not allowed to proceed.
     * @throws Throwable if any other error occurs.
     */
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        assertAuthorized(methodInvocation);
        return methodInvocation.proceed();
    }

    /**
     * Ensures the calling Subject is authorized to execute the specified <code>MethodInvocation</code>.
     * <p/>
     * As this is an AnnotationMethodInterceptor, this implementation merely delegates to the internal
     * {@link AuthorizingAnnotationHandler AuthorizingAnnotationHandler} by first acquiring the annotation by
     * calling {@link #getAnnotation(MethodInvocation) getAnnotation(methodInvocation)} and then calls
     * {@link AuthorizingAnnotationHandler#assertAuthorized(java.lang.annotation.Annotation) handler.assertAuthorized(annotation)}.
     *
     * @param mi the <code>MethodInvocation</code> to check to see if it is allowed to proceed/execute.
     * @throws AuthorizationException if the method invocation is not allowed to continue/execute.
     */
    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        try {
            ((AuthorizingAnnotationHandler)getHandler()).assertAuthorized(getAnnotation(mi));
        }
        catch(AuthorizationException ae) {
            // Annotation handler doesn't know why it was called, so add the information here if possible.
            // Don't wrap the exception here since we don't want to mask the specific exception, such as
            // UnauthenticatedException etc.
            if (ae.getCause() == null) ae.initCause(new AuthorizationException("Not authorized to invoke method: " + mi.getMethod()));
            throw ae;
        }
    }
}