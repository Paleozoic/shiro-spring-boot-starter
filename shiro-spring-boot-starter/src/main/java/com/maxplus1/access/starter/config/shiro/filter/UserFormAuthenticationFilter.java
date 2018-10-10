package com.maxplus1.access.starter.config.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class UserFormAuthenticationFilter extends FormAuthenticationFilter {


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)  throws Exception{
        /**
         * 如果是跳转到登录页面的请求
         */
        if (isLoginRequest(request, response)) {
            /**
             * 如果是登录提交
             */
            if (isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("[TRACE===>>>]Login submission detected.  Attempting to execute login.");
                }
                /**
                 * 执行登录操作
                 */
                return executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("[TRACE===>>>]Login page view.");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("[TRACE===>>>]Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }
            if(isAjax(request)){
                /**
                 * ajax请求，并且登录无效，报错
                 */
                response.getWriter().write(JacksonUtils.obj2Json(baseData));
            }else{
                /**
                 * 非ajax请求，跳转到登录页
                 */
                this.saveRequestAndRedirectToLogin(request, response);
            }
            return false;
        }
    }

    private static boolean isAjax(ServletRequest request){
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if("XMLHttpRequest".equalsIgnoreCase(header)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
