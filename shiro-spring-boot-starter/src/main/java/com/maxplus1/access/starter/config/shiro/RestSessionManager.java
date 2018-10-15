package com.maxplus1.access.starter.config.shiro;

import com.maxplus1.access.starter.config.shiro.utils.CookieUtils;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public class RestSessionManager extends DefaultWebSessionManager {

    @Autowired
    private ShiroProperties shiroProperties;

    public RestSessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest)request;
        String tokenKey = shiroProperties.getTokenKey();
        String sid = req.getHeader(tokenKey);
        if (sid!=null && sid.length()>0) {
            // 是否将sid保存到cookie，浏览器模式下可使用此参数。
            if (WebUtils.isTrue(request, tokenKey)){
                HttpServletResponse res = (HttpServletResponse)response;
                Cookie cookie = new SimpleCookie(tokenKey);
                cookie.setValue(sid); cookie.saveTo(req, res);
            }
        }else{
            sid = CookieUtils.getCookie(req,tokenKey);
        }
        // 设置当前session状态
        // session来源于url
        if (sid!=null && sid.length()>0) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,ShiroHttpServletRequest.URL_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sid);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        }
        return sid;
    }

}
