package com.maxplus1.access.starter.config.shiro.rbac.service;


import com.maxplus1.access.starter.config.shiro.rbac.Menu;

import java.util.List;


public interface IMenuService {
    /**
     * 获取用户已被授权的菜单
     * @param userId
     * @return
     */
    List<Menu> getUserMenuList(String userId);


}
