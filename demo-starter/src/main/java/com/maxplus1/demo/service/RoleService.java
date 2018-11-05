package com.maxplus1.demo.service;

import com.maxplus1.access.starter.config.shiro.rbac.ShiroRole;
import com.maxplus1.access.starter.config.shiro.rbac.service.IShiroRoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService implements IShiroRoleService {


    public List<ShiroRole> getUserRoleList(String userId) {
        List<ShiroRole> roleList = new ArrayList<ShiroRole>();
        for (int i = 0; i < 2; i++) {
            ShiroRole role = new ShiroRole();
            role.setRoleId("ROLE"+i);
            roleList.add(role);
        }
        return roleList;
    }
}
