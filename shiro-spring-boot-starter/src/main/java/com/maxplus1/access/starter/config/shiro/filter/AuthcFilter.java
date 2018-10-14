package com.maxplus1.access.starter.config.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthcFilter extends FormAuthenticationFilter {


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
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            StringBuffer sb = new StringBuffer();
            sb.append(" { ");
            sb.append("     \"code\":401, ");
            sb.append("     \"success\":false, ");
            sb.append("     \"data\":\"[ERROR===>>>]Please login first!\" ");
            sb.append(" } ");
            response.getWriter().write(sb.toString());

            return false;
        }
    }



}
