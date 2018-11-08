package com.maxplus1.access.starter.config.shiro.rbac.service;


import com.maxplus1.access.starter.config.shiro.rbac.ShiroMenu;
import com.maxplus1.access.starter.config.shiro.rbac.ShiroRole;
import com.maxplus1.access.starter.config.shiro.rbac.ShiroUser;

import java.util.List;


public interface IShiroService {
    /**
     * 获取用户已被授权的菜单
     * @param userId
     * @return
     */
    List<ShiroMenu> getUserMenuList(String userId);


    /**
     * 用户已授权的角色
     * @param userId
     * @return
     */
    List<ShiroRole> getUserRoleList(String userId);

    /**
     * 通过用户名获取用户信息，包括密码
     * 用户信息会存储在Session，密码会脱敏
     * @param userName
     * @return
     */
    ShiroUser getUserByNameWithPassword(String userName);
}
