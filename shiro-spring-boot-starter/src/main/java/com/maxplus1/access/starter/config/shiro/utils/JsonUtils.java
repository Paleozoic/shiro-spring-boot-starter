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
            /**
             * 不需要flush  and close
             * https://blog.csdn.net/VIP_WangSai/article/details/78357018
             * Normally you should not close the stream. The servlet container will automatically close the stream after the servlet is finished running as part of the servlet request life-cycle.
             * For instance, if you closed the stream it would not be available if you implemented a Filter.
             * Having said all that, if you do close it nothing bad will happen as long as you don't try to use it again.
             */
//            response.getWriter().flush();
//            response.getWriter().close();
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
