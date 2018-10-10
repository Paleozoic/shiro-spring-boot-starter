package com.maxplus1.access.starter.config.shiro.rbac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class User<T> implements Serializable {
    private String userId;
    private String userName;
    private String deptId;
    private String deptName;
    private String realName;
    private String status;
    private String password;
    /**
     * 扩展其他参数的模板
     */
    private T obj;


}
