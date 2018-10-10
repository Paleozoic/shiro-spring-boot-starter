package com.maxplus1.demo.service;

import com.maxplus1.access.starter.config.shiro.rbac.Menu;
import com.maxplus1.access.starter.config.shiro.rbac.service.IMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService implements IMenuService{


    public List<Menu> getUserMenuList(String userId) {
        List<Menu> menuList = new ArrayList<Menu>();
        for (int i = 0; i < 10; i++) {
            Menu menu = new Menu();
            menu.setMenuId("MENU"+i);
            menu.setMenuCode("MENU_CODE"+i);
            menuList.add(menu);
        }
        return menuList;
    }
}
