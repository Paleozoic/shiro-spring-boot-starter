package com.maxplus1.demo.service;

import com.maxplus1.access.starter.config.shiro.rbac.User;
import com.maxplus1.access.starter.config.shiro.rbac.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    public User getUserByNameWithPassword(String userName) {
        User u = new User();
        u.setUserId("USER0");
        u.setUserName("用户0");
        u.setPassword("PASS0");
        return u;
    }

}
