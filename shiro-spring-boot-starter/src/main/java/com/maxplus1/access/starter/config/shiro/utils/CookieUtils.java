package com.maxplus1.access.starter.config.shiro.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CookieUtils {


    public static String getCookie(HttpServletRequest req, String name) {
        javax.servlet.http.Cookie[] cookies = req.getCookies();
        if(cookies!=null && cookies.length>0){
            for (javax.servlet.http.Cookie cookie : cookies) {
                if(name.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
