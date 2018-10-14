package com.maxplus1.access.starter.config.shiro;

import com.maxplus1.access.starter.config.shiro.interceptor.WebResource;
import com.maxplus1.access.starter.config.shiro.rbac.AccUtils;
import com.maxplus1.access.starter.config.shiro.rbac.User;
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
import java.io.IOException;


@Slf4j
public abstract class BaseController {

    private final static Integer LOGIN_FAIL = HttpServletResponse.SC_UNAUTHORIZED;
    private final static Integer AUTH_FAIL = HttpServletResponse.SC_FORBIDDEN;

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



    /**
     * 登录认证异常
     */
    @ExceptionHandler({ UnauthenticatedException.class, AuthenticationException.class })
    public void authcExp(HttpServletResponse response) {
        writeJson(LOGIN_FAIL,"Please login first!",response);
    }

    /**
     * 权限异常
     */
    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
    public void permsExp(HttpServletRequest request, HttpServletResponse response) {
        writeJson(AUTH_FAIL,"No permission!",response);
    }


    private void writeJson(Integer code,String msg, HttpServletResponse response) {
        StringBuffer sb = new StringBuffer();
        sb.append(" { ");
        sb.append("     \"code\":"+code+", ");
        sb.append("     \"success\":false, ");
        sb.append("     \"data\":\"[ERROR===>>>]"+msg+"\" ");
        sb.append(" } ");
        try {
            response.getWriter().write(sb.toString());
        } catch (IOException e) {
           log.error("[ERROR===>>>]"+e.getMessage(),e);
        }
    }

}

