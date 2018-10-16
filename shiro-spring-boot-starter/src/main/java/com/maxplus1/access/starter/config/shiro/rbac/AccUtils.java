package com.maxplus1.access.starter.config.shiro.rbac;


import com.maxplus1.access.starter.config.shiro.interceptor.WebResource;

public class AccUtils {

    private final static String USER_ID_SESSION_KEY = "session:com.maxplus1.access.config.shiro.UserId";
    private final static String USER_SESSION_KEY = "session:com.maxplus1.access.config.shiro.ShiroUser";

    /**
     * 从会话中取出userId
     * @return
     */
    public static String getUserId() {
        Object obj = WebResource.session().getAttribute(USER_ID_SESSION_KEY);
        if (obj != null && obj.getClass() == String.class) {
            return (String)obj;
        } else {
            return null;
        }
    }

    /**
     * 将userId加入会话
     * @return
     */
    public static void setUserId(String userId) {
        WebResource.session().setAttribute(USER_ID_SESSION_KEY, userId);
    }

    public static void setUser(ShiroUser user){
        WebResource.session().setAttribute(USER_SESSION_KEY, user);
    }
    public static ShiroUser getUser(){
        Object obj = WebResource.session().getAttribute(USER_SESSION_KEY);
        if (obj != null && obj.getClass() == ShiroUser.class) {
            return (ShiroUser)obj;
        } else {
            return null;
        }
    }

}