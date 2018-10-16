package com.maxplus1.access.starter.config.shiro.rbac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ShiroMenu<T> implements Serializable{
    private String menuId;
    private String menuCode;
    private String upperMenuId;
    private String upperMenuName;
    private String menuName;
    private String menuType;
    private Integer orderNo;
    private String url;
    private Integer status;
    private String appId;
    /**
     * 扩展其他参数的模板
     */
    private T obj;

}
