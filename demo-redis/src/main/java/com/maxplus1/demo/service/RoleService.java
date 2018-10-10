package com.maxplus1.demo.service;

import com.maxplus1.access.starter.config.shiro.rbac.Role;
import com.maxplus1.access.starter.config.shiro.rbac.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService implements IRoleService {


    public List<Role> getUserRoleList(String userId) {
        List<Role> roleList = new ArrayList<Role>();
        for (int i = 0; i < 2; i++) {
            Role role = new Role();
            role.setRoleId("ROLE"+i);
            roleList.add(role);
        }
        return roleList;
    }
}
