package com.maxplus1.access.starter.config.shiro.rbac.service;


import com.maxplus1.access.starter.config.shiro.rbac.ShiroUser;

public interface IShiroUserService {


    ShiroUser getUserByNameWithPassword(String userName);

}
