package com.maxplus1.access.starter.config.shiro.bean;

import lombok.Data;

@Data
public class App {

    /**
     * 当前系统的标识ID
     */
    private String id;
    /**
     * 在header或者cookie传递的应用表示入参
     */
    private String key;
}
