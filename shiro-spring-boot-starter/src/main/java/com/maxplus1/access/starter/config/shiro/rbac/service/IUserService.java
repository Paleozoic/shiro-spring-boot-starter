package com.maxplus1.access.starter.config.shiro.rbac.service;


import com.maxplus1.access.starter.config.shiro.rbac.User;

public interface IUserService {


    User getUserByNameWithPassword(String userName);

}
