package com.maxplus1.access.starter.config.shiro;

import com.maxplus1.access.starter.config.shiro.interceptor.WebResource;
import com.maxplus1.access.starter.config.shiro.rbac.AccUtils;
import com.maxplus1.access.starter.config.shiro.rbac.User;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BaseController {
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


}

