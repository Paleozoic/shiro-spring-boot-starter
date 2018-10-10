package com.maxplus1.access.starter.config.shiro.rbac.service;



import com.maxplus1.access.starter.config.shiro.rbac.Role;

import java.util.List;

public interface IRoleService {

    /**
     * 用户已授权的角色
     * @param userId
     * @return
     */
    List<Role> getUserRoleList(String userId);
}
