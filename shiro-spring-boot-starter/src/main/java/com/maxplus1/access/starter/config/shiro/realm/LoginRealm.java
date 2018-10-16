package com.maxplus1.access.starter.config.shiro.realm;


import com.maxplus1.access.starter.config.shiro.rbac.ShiroMenu;
import com.maxplus1.access.starter.config.shiro.rbac.ShiroRole;
import com.maxplus1.access.starter.config.shiro.rbac.ShiroUser;
import com.maxplus1.access.starter.config.shiro.rbac.service.IShiroMenuService;
import com.maxplus1.access.starter.config.shiro.rbac.service.IShiroRoleService;
import com.maxplus1.access.starter.config.shiro.rbac.service.IShiroUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

public class LoginRealm extends AuthorizingRealm {

    @Autowired
    private IShiroUserService userService;
    @Autowired
    private IShiroRoleService roleService;
    @Autowired
    private IShiroMenuService menuService;

    /**
     * 用户权限验证，这一步获取权限信息
     * TODO 缓存
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String userId =  (String) principals.getPrimaryPrincipal();
        if( userId != null ){
            // 查询用户授权信息
            List<ShiroRole> userRoles=roleService.getUserRoleList(userId);
            List<ShiroMenu> userMenus =menuService.getUserMenuList(userId);
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
        ShiroUser user = userService.getUserByNameWithPassword(userName);
        if(user!=null){

            //这一步传入数据库查找的用户密码，返回SimpleAuthenticationInfo，Shiro会通过SimpleAuthenticationInfo的password和UsernamePasswordToken的password进行匹配
            String pwd = user.getPassword();

            return new SimpleAuthenticationInfo(user.getUserId(),pwd,this.getName());
        }else{
            throw new UnknownAccountException("[ERROR===>>>]用户不存在！");
        }
    }

}
