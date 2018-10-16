package com.maxplus1.access.starter.config.shiro.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JsonUtils {


    private final static Integer LOGIN_FAIL = HttpServletResponse.SC_UNAUTHORIZED;
    private final static Integer AUTH_FAIL = HttpServletResponse.SC_FORBIDDEN;

    public static void writeJson(Integer code,String msg, HttpServletResponse response) {
        StringBuffer sb = new StringBuffer();
        sb.append(" { ");
        sb.append("     \"code\":"+code+", ");
        sb.append("     \"success\":false, ");
        sb.append("     \"data\":\"[ERROR===>>>]"+msg+"\" ");
        sb.append(" } ");
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(sb.toString());
        } catch (IOException e) {
            log.error("[ERROR===>>>]"+e.getMessage(),e);
        }
    }


    public static void loginFail(HttpServletResponse response){
        JsonUtils.writeJson(LOGIN_FAIL,"Please login first!",response);
    }

    public static void authFail(HttpServletResponse response){
        JsonUtils.writeJson(AUTH_FAIL,"No permission!",response);
    }

    public static void appFail(String appId,HttpServletResponse response){
        JsonUtils.writeJson(AUTH_FAIL,"[ERROR===>>>]No permission to access the App [" + appId + "]",response);
    }
}
