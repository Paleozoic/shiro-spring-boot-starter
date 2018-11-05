package com.maxplus1.demo.service;

import com.maxplus1.access.starter.config.shiro.rbac.ShiroUser;
import com.maxplus1.access.starter.config.shiro.rbac.service.IShiroUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IShiroUserService {

    public ShiroUser getUserByNameWithPassword(String userName) {
        ShiroUser u = new ShiroUser();
        u.setUserId("USER0");
        u.setUserName("用户0");
        u.setPassword("PASS0");
        return u;
    }

}
