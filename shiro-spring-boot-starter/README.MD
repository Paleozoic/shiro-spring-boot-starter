# 登录鉴权方式
-  使用传统的session方式

# JWT和Session的区别
- JWT：服务端无状态，所有状态存储在客户端的加密token里面。服务端只负责校验token的有效性。服务端无法强制下线。
- Session：服务端有状态。服务端只是存储一个sid，服务端存储sid对应的信息。多应用共享Session通常使用Redis异类的内存数据库存储。

# 配置方式
- 放在header或者cookie的sessionId。header的sid具有高优先级。



# 注意
- 此包只适配了前后端分离的项目，没有对静态资源进行处理


# Spring Boot SPI 
Spring Boot SPI 可以代替包扫描的配置
Spring Boot SPI 类似于 Java SPI的加载机制。
META-INF/spring.factories配置：
```
# 如下：
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
```


# Session的序列化
- 由于Session都会实现Serializable，所以统一使用JDK的序列化方式，保证稳定性和兼容性。

# Redis配置
- Redis配置完全和spring-data-redis一致，且为默认的RedisTemplate
- 由于Shiro的序列化方式默认是JDK序列化，如果非Shiro需要使用其他序列化方式，需要单独配置，参考demo。
- 多Redis数据源配置：此时`spring.redis`的配置依然需要配置，其他数据源单独配置。

# 配置
```yml
spring:
    redis:
      database: 0
      host: 192.168.0.105
      port: 6379
      password: foobared
      lettuce:
        pool:
          max-active: 8
          max-wait: -1
          max-idle: 8
          min-idle: 0
      timeout: 100s
    maxplus1:
        shiro:
            tokenKey: uuusid
            loginUrl: /api/sys/login
            filterChainDefinitionMap:
                "/static/**": "anon"
                "/api/sys/ssoLogin": "anon"
                "/api/sys/login": "anon"
                "/api/sys/testRedis": "anon"
                "/api/**": "perms"
                "/**": "authc"
                "/api/sys/logout": "logout"
            globalSessionTimeout: 180000000 # 3,600,000 milliseconds = 1 hour
            sessionValidationInterval: 360000 # 会话有效校验扫描间隔
            redisCacheEnabled: true # 开启分布式session 需要引入Redis的相关配置，spring-data-redis
            testMode: false # 开启测试模式，测试模式下所有url的访问权限都是anon
            mockUser: # 测试模式模拟的用户
                userId: 'MOCK_USER0'
                userName: 'mock_yonghu0'
                deptId: 'MOCK_DEPT0'
                deptName: '模拟部门0'
                realName: '模拟用户0'
                status: '正常'
                password: 'MOCK_PASS0'

```

# demo测试
- 启动demo or  demo-redis
- 登录测试（返回SessionId）：127.0.0.1:9000/demo/api/sys/login?userName=yonghu0&password=PASS0
- 访问测试（需要在cookie或者header带上uuusid=sessionId访问）：127.0.0.1:9000/demo/api/sys/testAccess
- 无权限访问测试（需要在cookie或者header带上uuusid=sessionId访问）：127.0.0.1:9000/demo/api/sys/testDeny
- 测试不同的Redis序列化方式：127.0.0.1:9000/demo/api/sys/testRedis

# HTTP状态码
-  未登录被拒绝：401 （未授权） 请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应。
-  未授权被拒绝：403 （禁止） 服务器拒绝请求。 

# 注意
- 既然基于Shiro做了封装，那么可自定义的模块肯定减少，如果需要增加其他配置。可以：
    - 下载源码自行修改
    - 提issue到github
    - 使用原生Shiro进行个性化的封装
    
    
    
# TODO
- Session的一级缓存，二级缓存  
- 动态更新权限，刷新缓存
- 同一账号多处登录问题
- 账户踢出接口
- Shiro其他配置：http://shiro.apache.org/spring-boot.html#configuration-properties    