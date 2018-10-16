package com.maxplus1.access.starter.config.shiro.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalMap;

import java.util.Map;

public class ShiroUtils {

    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";

    public static String getUserId(PrincipalCollection principalCollection) {
        SimplePrincipalMap simplePrincipalMap = (SimplePrincipalMap) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        for (Map.Entry<String, Object> entry : simplePrincipalMap.entrySet()) {
            if(USER_ID.equals(entry.getKey())){
                return entry.getValue().toString();
            }
        }
        return null;
    }


    public static String getUserName(PrincipalCollection principalCollection) {
        SimplePrincipalMap simplePrincipalMap = (SimplePrincipalMap) SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        for (Map.Entry<String, Object> entry : simplePrincipalMap.entrySet()) {
            if(USER_NAME.equals(entry.getKey())){
                return entry.getValue().toString();
            }
        }
        return null;
    }
}
