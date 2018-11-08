package com.maxplus1.demo.service;

import com.maxplus1.access.starter.config.shiro.rbac.ShiroMenu;
import com.maxplus1.access.starter.config.shiro.rbac.ShiroRole;
import com.maxplus1.access.starter.config.shiro.rbac.ShiroUser;
import com.maxplus1.access.starter.config.shiro.rbac.service.IShiroService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShiroService implements IShiroService {


    public List<ShiroMenu> getUserMenuList(String userId) {
        List<ShiroMenu> menuList = new ArrayList<ShiroMenu>();
        for (int i = 0; i < 10; i++) {
            ShiroMenu menu = new ShiroMenu();
            menu.setMenuId("MENU" + i);
            menu.setMenuCode("MENU_CODE" + i);
            menuList.add(menu);
        }
        return menuList;
    }

    public List<ShiroRole> getUserRoleList(String userId) {
        List<ShiroRole> roleList = new ArrayList<ShiroRole>();
        for (int i = 0; i < 2; i++) {
            ShiroRole role = new ShiroRole();
            role.setRoleId("ROLE" + i);
            roleList.add(role);
        }
        return roleList;
    }

    public ShiroUser getUserByNameWithPassword(String userName) {
        ShiroUser u = new ShiroUser();
        u.setUserId("USER0");
        u.setUserName("用户0");
        u.setPassword("PASS0");
        return u;
    }
}
