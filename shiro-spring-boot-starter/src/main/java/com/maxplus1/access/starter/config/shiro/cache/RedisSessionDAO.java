package com.maxplus1.access.starter.config.shiro.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisSessionDAO extends AbstractSessionDAO {


    /**
     * Session在redis里面的默认key前缀
     */
    private static final String DEFAULT_SESSION_KEY_PREFIX = "maxplus1:shiro:session:";
    /**
     * TODO 可配置
     */
    private String keyPrefix = DEFAULT_SESSION_KEY_PREFIX;

    private static final long DEFAULT_SESSION_IN_MEMORY_TIMEOUT = 1000L;
    /**
     * doReadSession be called about 10 times when login.
     * Save Session in ThreadLocal to resolve this problem. sessionInMemoryTimeout is expiration of Session in ThreadLocal.
     * The default value is 1000 milliseconds (1s).
     * Most of time, you don't need to change it.
     */
    private long sessionInMemoryTimeout = DEFAULT_SESSION_IN_MEMORY_TIMEOUT;

    // expire time in seconds
    private static final int DEFAULT_EXPIRE = -2;
    private static final int NO_EXPIRE = -1;

    /**
     * Please make sure expire is longer than sesion.getTimeout()
     */
    /**
     * TODO 可配置
     */
    private int expire = DEFAULT_EXPIRE;

    private RedisTemplate<String,Session> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Session> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static ThreadLocal sessionsInThread = new ThreadLocal();

    @Override
    public void update(Session session) throws UnknownSessionException {
        this.saveSession(session);
    }

    /**
     * save session
     * @param session
     * @throws UnknownSessionException
     */
    private void saveSession(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            log.error("[ERROR===>>>]session or session id is null");
            throw new UnknownSessionException("[ERROR===>>>]session or session id is null");
        }
        ValueOperations<String, Session> ops = redisTemplate.opsForValue();
        if (expire == DEFAULT_EXPIRE) {
            ops.set(session.getId().toString(),session,session.getTimeout(), TimeUnit.MILLISECONDS);
            return;
        }
        if (expire != NO_EXPIRE &&  TimeUnit.MILLISECONDS.toSeconds(expire) < session.getTimeout()) {
            log.warn("[WARN===>>>]Redis session expire time(SECONDS): {} is less than Session timeout: {} .It may cause some problems.",
                    TimeUnit.MILLISECONDS.toSeconds(expire),session.getTimeout() );
        }
        ops.set(session.getId().toString(),session,expire, TimeUnit.MILLISECONDS);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            log.error("[ERROR===>>>]session or session id is null");
            return;
        }
        redisTemplate.delete(session.getId().toString());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<Session>();

        Set<String> keys = redisTemplate.keys(this.keyPrefix + "*");
        ValueOperations<String, Session> ops = redisTemplate.opsForValue();
        if (keys != null && keys.size() > 0) {
            for (String key:keys) {
                Session s = ops.get(key);
                sessions.add(s);
            }
        }
        return sessions;
    }

    @Override
    protected Serializable doCreate(Session session) {
        if (session == null) {
            log.error("[ERROR===>>>]session is null");
            throw new UnknownSessionException("session is null");
        }
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            log.warn("[WARN===>>>]session id is null");
            return null;
        }
        Session s = getSessionFromThreadLocal(sessionId);

        if (s != null) {
            return s;
        }
        log.debug("[DEBUG===>>>]read session from redis");
        ValueOperations<String, Session> ops = redisTemplate.opsForValue();
        s =  ops.get(sessionId);
        setSessionToThreadLocal(sessionId, s);
        return s;
    }

    private void setSessionToThreadLocal(Serializable sessionId, Session s) {
        Map<Serializable, SessionInMemory> sessionMap = (Map<Serializable, SessionInMemory>) sessionsInThread.get();
        if (sessionMap == null) {
            sessionMap = new HashMap<Serializable, SessionInMemory>();
            sessionsInThread.set(sessionMap);
        }
        SessionInMemory sessionInMemory = new SessionInMemory();
        sessionInMemory.setCreateTime(new Date());
        sessionInMemory.setSession(s);
        sessionMap.put(sessionId, sessionInMemory);
    }

    private Session getSessionFromThreadLocal(Serializable sessionId) {
        Session s = null;

        if (sessionsInThread.get() == null) {
            return null;
        }

        Map<Serializable, SessionInMemory> sessionMap = (Map<Serializable, SessionInMemory>) sessionsInThread.get();
        SessionInMemory sessionInMemory = sessionMap.get(sessionId);
        if (sessionInMemory == null) {
            return null;
        }
        Date now = new Date();
        long duration = now.getTime() - sessionInMemory.getCreateTime().getTime();
        if (duration < sessionInMemoryTimeout) {
            s = sessionInMemory.getSession();
            log.debug("[DEBUG===>>>]read session from memory");
        } else {
            sessionMap.remove(sessionId);
        }

        return s;
    }

}