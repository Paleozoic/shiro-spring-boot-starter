package com.maxplus1.access.starter.config.shiro.cache;

import lombok.Data;
import org.apache.shiro.session.Session;

import java.util.Date;

/**
 * Use ThreadLocal as a temporary storage of Session, so that shiro wouldn't keep read redis several times while a request coming.
 */
@Data
public class SessionInMemory {
    private Session session;
    private Date createTime;


}
