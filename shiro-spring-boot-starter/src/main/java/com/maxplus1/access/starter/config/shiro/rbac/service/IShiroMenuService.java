package com.maxplus1.access.starter.config.shiro.rbac.service;


import com.maxplus1.access.starter.config.shiro.rbac.ShiroMenu;

import java.util.List;


public interface IShiroMenuService {
    /**
     * 获取用户已被授权的菜单
     * @param userId
     * @return
     */
    List<ShiroMenu> getUserMenuList(String userId);


}
