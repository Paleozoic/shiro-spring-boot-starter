package com.maxplus1.access.starter.config.shiro;

import com.maxplus1.access.starter.config.shiro.interceptor.WebResource;
import com.maxplus1.access.starter.config.shiro.rbac.AccUtils;
import com.maxplus1.access.starter.config.shiro.rbac.User;
import com.maxplus1.access.starter.config.shiro.utils.CookieUtils;
import com.maxplus1.access.starter.config.shiro.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
public abstract class BaseController {



    @Autowired
    private ShiroProperties shiroProperties;

    /**
     * 获得响应
     * @return
     */
    public HttpServletRequest req(){
        return WebResource.request();
    }

    /**
     * 获得请求
     * @return
     */
    public HttpServletResponse res(){
        return WebResource.response();
    }

    /**
     * 获得web上下文
     * @return
     */
    public WebApplicationContext cxt() {
        return WebResource.webcontext();
    }

    /**
     * 获得会话
     * @return
     */
    public Session session() {
        return WebResource.session();
    }

    /**
     * 获取uri
     * @return
     */
    public String uri(){
        return req().getRequestURI();
    }

    /**
     * 获取当前系统标识符
     * @return
     */
    public String appId(){
        return shiroProperties.getApp().getId();
    }


    /**
     * 通过协议传参获取系统标识符
     * @return
     */
    public String appThirdId(){
        String key = shiroProperties.getApp().getKey();
        HttpServletRequest req = req();
        String appId = req.getHeader(key);
        if (appId==null || appId.length()<=0) {
            appId = CookieUtils.getCookie(req,key);
        }
        return appId;
    }

    /**
     * 获取当前登录用户userId
     * @return
     */
    public String userId(){
        if(shiroProperties.getTestMode()){
            return shiroProperties.getMockUser().getUserId();
        }
        return AccUtils.getUserId();
    }

    public String deptId(){
        if(shiroProperties.getTestMode()){
            return shiroProperties.getMockUser().getDeptId();
        }
        return user().getDeptId();
    }

    public User user(){
        if(shiroProperties.getTestMode()){
            return shiroProperties.getMockUser();
        }
        return AccUtils.getUser();
    }



    /**
     * 登录认证异常
     */
    @ExceptionHandler({ UnauthenticatedException.class, AuthenticationException.class })
    public void authcExp(HttpServletResponse response) {
        JsonUtils.loginFail(response);
    }

    /**
     * 权限异常
     */
    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
    public void permsExp(HttpServletResponse response) {
        JsonUtils.authFail(response);
    }




}

