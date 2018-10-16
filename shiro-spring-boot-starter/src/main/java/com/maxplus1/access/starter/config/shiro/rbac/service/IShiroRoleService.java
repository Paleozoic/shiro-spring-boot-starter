package com.maxplus1.access.starter.config.shiro.rbac.service;



import com.maxplus1.access.starter.config.shiro.rbac.ShiroRole;

import java.util.List;

public interface IShiroRoleService {

    /**
     * 用户已授权的角色
     * @param userId
     * @return
     */
    List<ShiroRole> getUserRoleList(String userId);
}
