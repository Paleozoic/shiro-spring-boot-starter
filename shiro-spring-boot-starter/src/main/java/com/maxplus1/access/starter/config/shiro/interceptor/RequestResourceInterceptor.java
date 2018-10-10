package com.maxplus1.access.starter.config.shiro.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取请求资源的拦截器，
 * 主要用来获取请求和响应
 * @author Paleo
 */
public class RequestResourceInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        WebResource.REQUEST_THREAD.set(request);
        WebResource.RESPONSE_THREAD.set(response);
        //开启跨域请求
//		response.setHeader("Access-Control-Allow-Origin", "*");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        WebResource.REQUEST_THREAD.remove();
        WebResource.RESPONSE_THREAD.remove();
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

}