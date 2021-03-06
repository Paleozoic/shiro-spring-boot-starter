package com.maxplus1.access.starter.config.shiro.realm;


import com.maxplus1.access.starter.config.shiro.rbac.ShiroMenu;
import com.maxplus1.access.starter.config.shiro.rbac.ShiroRole;
import com.maxplus1.access.starter.config.shiro.rbac.ShiroUser;
import com.maxplus1.access.starter.config.shiro.rbac.service.IShiroService;
import com.maxplus1.access.starter.config.shiro.utils.ShiroUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LoginRealm extends AuthorizingRealm {

    @Autowired
    private IShiroService shiroService;


    /**
     * 用户权限验证，这一步获取权限信息
     * TODO 缓存
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userId = ShiroUtils.getUserId();
        if( userId != null ){
            // 查询用户授权信息
            List<ShiroRole> userRoles=shiroService.getUserRoleList(userId);
            List<ShiroMenu> userMenus =shiroService.getUserMenuList(userId);
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            if( userRoles != null && !userRoles.isEmpty() ){
                for( ShiroRole role:userRoles ){
                    info.addRole(role.getRoleId());
                }
            }
            if( userMenus != null && !userMenus.isEmpty() ){
                for( ShiroMenu menu:userMenus ){
                    info.addStringPermission(menu.getMenuCode());
                }
            }
            return info;
        }
        return null;
    }

    /**
     * 用户登陆验证，这一步获取登陆信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) {
        UsernamePasswordToken token = (UsernamePasswordToken) authToken;
        String userName = token.getUsername();
        String password = String.valueOf(token.getPassword());
        //提交登陆表单，会进入这里2次。第一次userName为null，password有值。第二次userName、password都有值。原因不明。
        if(userName==null||password==null) {return null;}
        ShiroUser user = shiroService.getUserByNameWithPassword(userName);
        if(user!=null){

            //这一步传入数据库查找的用户密码，返回SimpleAuthenticationInfo，Shiro会通过SimpleAuthenticationInfo的password和UsernamePasswordToken的password进行匹配
            String pwd = user.getPassword();
            SimplePrincipalMap simplePrincipalMap = new SimplePrincipalMap();
            simplePrincipalMap.put(ShiroUtils.USER_ID,user.getUserId());
            simplePrincipalMap.put(ShiroUtils.USER_NAME,user.getUserName());
            return new SimpleAuthenticationInfo(simplePrincipalMap,pwd,this.getName());
        }else{
            throw new UnknownAccountException("[ERROR===>>>]用户不存在！");
        }
    }

}
