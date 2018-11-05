package com.maxplus1.demo.service;

import com.maxplus1.access.starter.config.shiro.rbac.ShiroMenu;
import com.maxplus1.access.starter.config.shiro.rbac.service.IShiroMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService implements IShiroMenuService {


    public List<ShiroMenu> getUserMenuList(String userId) {
        List<ShiroMenu> menuList = new ArrayList<ShiroMenu>();
        for (int i = 0; i < 10; i++) {
            ShiroMenu menu = new ShiroMenu();
            menu.setMenuId("MENU"+i);
            menu.setMenuCode("MENU_CODE"+i);
            menuList.add(menu);
        }
        return menuList;
    }
}
