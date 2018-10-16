package com.maxplus1.access.starter.config.shiro.rbac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Role<T> implements Serializable {
    private String roleId;
    private String roleName;
    private String roleGroupId;
    private String roleGroupName;
    private Integer status;
    private String appId;
    /**
     * 扩展其他参数的模板
     */
    private T obj;
}

