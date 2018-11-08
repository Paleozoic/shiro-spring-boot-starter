package com.maxplus1.access.starter.config.shiro.interceptor;


import com.maxplus1.access.starter.config.shiro.rbac.AccUtils;
import com.maxplus1.access.starter.config.shiro.rbac.ShiroUser;
import com.maxplus1.access.starter.config.shiro.rbac.service.IShiroService;
import com.maxplus1.access.starter.config.shiro.utils.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 登陆拦截器，存储一些账号信息进session
 * @author Paleo
 *
 */
public class LoginInterceptor implements HandlerInterceptor{

    @Autowired
    private IShiroService shiroService;


    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
              {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex)
              {
        if(SecurityUtils.getSubject().isAuthenticated()){
            String userId = ShiroUtils.getUserId();
            String userName = ShiroUtils.getUserName();
            AccUtils.setUserId(userId);
            ShiroUser user = shiroService.getUserByNameWithPassword(userName);
            // 密码脱敏
            user.setPassword("*******");
            AccUtils.setUser(user);
        }
    }

}
