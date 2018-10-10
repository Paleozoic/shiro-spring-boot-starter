package com.maxplus1.demo.rest;

import com.maxplus1.access.starter.config.shiro.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sys")
@Slf4j
public class SysRest extends BaseController{

    @Resource(name = "shiroRedisTemplate")
    private RedisTemplate shiroRedisTemplate;


    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    @GetMapping("login")
    public String login(String userName,String password ) throws Exception {

        UsernamePasswordToken token = new UsernamePasswordToken(userName, password, false);
        try {
            SecurityUtils.getSubject().login(token);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new Exception("[ERROR===>>>]用户名或密码错误！");
        }
        return SecurityUtils.getSubject().getSession().getId().toString();
    }


    @GetMapping("testAccess")
    @RequiresPermissions("MENU_CODE0")
    public Map access( )  {
        Map map = new HashMap();
        map.put("当前登录用户ID",userId());
        return map;
    }

    @GetMapping("testDeny")
    @RequiresPermissions("MENU_CODE_UN")
    public String deny( )  {
        return "[ERROR===>>>]RequiresPermissions is not valid";
    }


    @GetMapping("testRedis")
    public Map redis( )  {
        Map map = new HashMap();
        map.put("shiroRedisTemplate",shiroRedisTemplate.getValueSerializer().getClass());
        map.put("redisTemplate",redisTemplate.getValueSerializer().getClass());
        return map;
    }

}
