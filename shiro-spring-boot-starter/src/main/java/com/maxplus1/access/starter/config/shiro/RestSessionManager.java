package com.maxplus1.access.starter.config.shiro;

import org.apache.commons.lang3.StringUtils;
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
        HttpServletRequest rq = (HttpServletRequest)request;
        String sid = rq.getHeader(shiroProperties.getTokenKey());
        if (StringUtils.isNotBlank(sid)) {
            // 是否将sid保存到cookie，浏览器模式下可使用此参数。
            if (WebUtils.isTrue(request, shiroProperties.getTokenKey())){
                HttpServletResponse rs = (HttpServletResponse)response;
                Cookie template = getSessionIdCookie();
                Cookie cookie = new SimpleCookie(template);
                cookie.setValue(sid); cookie.saveTo(rq, rs);
            }
            // 设置当前session状态
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                    ShiroHttpServletRequest.URL_SESSION_ID_SOURCE); // session来源于url
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sid);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return sid;
        }else{
            return super.getSessionId(request, response);
        }
    }

}
