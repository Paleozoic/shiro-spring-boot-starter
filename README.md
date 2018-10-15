# 写在前面
- 此starter封装并不只是为了使用完整的Shiro功能，目的是为大家提供一种封装思路，如果实际应用在业务场景，还需要具体情况具体分析
- 当然你也可以遵循目前的设计规范，如果有功能没有满足，不妨提个issue……
- 倘若你有自己的设计，fork过去尽情修改吧~

# JWT和Session的区别
- JWT：服务端无状态。所有状态存储在客户端的加密token里面。服务端只负责校验token的有效性。服务端无法强制下线。
- Session：服务端有状态。服务端只是存储一个sid，服务端存储sid对应的信息。多应用共享Session通常使用Redis之类的内存数据库存储。

# 登录鉴权方式
-  使用传统的session方式

# SessionId传递
- sessionId放在header或者cookie进行传递。header的sid具有高优先级。


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
- 由于Shiro的序列化方式默认是JDK序列化，如果非Shiro需要使用其他序列化方式，需要单独配置，参考demo-redis。
- 多Redis数据源配置：此时`spring.redis`的配置依然需要配置，其他数据源单独配置。

# appId
- appId用于多系统交互时使用，如果系统不涉及此方面，可以忽略。
- appId可以通过配置文件配置，表示当前系统的app表示
- 也可以遵循协议，appId通过cookie或者header传递。header具有较高优先级（设计和Session一样）
- 为什么不放在Session？
    - 因为多系统可能公用一个Session，此时无法判断当前Session属于哪个系统。

# 配置文件示例
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
          app:
            id: uuuappkey
            key: MaxPlus1     
          tokenKey: uuusid
          loginUrl: /api/sys/login
          filterChain: | # 注意所有perms的权限都需要通过@RequirePermissions来实现，建议不要配置perms（动态URL问题）。PS:yaml配置map的key含有/时，无法识别/。
              /static/**=anon
              /api/sys/ssoLogin=anon
              /api/sys/login=anon
              /api/sys/testRedis=anon
              /api/**=authc
              /**=authc
              /api/sys/logout=logout
          globalSessionTimeout: 180000000 # 3,600,000 milliseconds = 1 hour
          sessionValidationInterval: 360000 # 会话有效校验扫描间隔
          redisCacheEnabled: false # 开启分布式session 需要引入Redis的相关配置，spring-data-redis
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

# Filters与AuthorizingAnnotationMethodInterceptor
- `@RequiresPermissions`注解的处理是通过AOP实现拦截器`PermissionAnnotationMethodInterceptor`来处理；而`perms`是通过filter来实现的：`PermissionsAuthorizationFilter`。
- Filter与MethodInterceptor的区别，Filter是基于URL做的拦截，而MethodInterceptor是基于AOP直接对方法进行的拦截。对于REST URL，比如`/api/user/{id}`这样的url请求。
- Filter处理REST URL需要自己实现Filter并对url进行解析匹配（具体参考spring mvc的url匹配规则，略复杂）；或者直接使用MethodInterceptor。


# demo测试
- 启动demo or  demo-redis
- 登录测试（返回SessionId）：127.0.0.1:9000/demo/api/sys/login?userName=yonghu0&password=PASS0
- 访问测试（需要在cookie或者header带上uuusid=sessionId访问）：127.0.0.1:9000/demo/api/sys/testAccess
- 无权限访问测试（需要在cookie或者header带上uuusid=sessionId访问）：127.0.0.1:9000/demo/api/sys/testDeny
- 测试不同的Redis序列化方式：127.0.0.1:9000/demo/api/sys/testRedis

# HTTP状态码
-  未登录被拒绝：401 
-  未授权被拒绝：403 

# 注意
- 既然基于Shiro做了封装，那么可自定义的模块肯定减少，如果需要增加其他配置。可以：
    - 下载源码自行修改
    - 提issue到github
    - 使用原生Shiro进行个性化的封装
- `com.maxplus1.access.starter.config.shiro.interceptor.shiro.Perms` (已废弃)  
- 此包只适配了前后端分离的项目，没有对静态资源进行处理。且Shiro的异常以json形式返回。    

# deploy
- 更改parent下的pom.xml的`deploy2maven.url.snapshots`和`deploy2maven.url.releases`为自己私库的url
- 配置maven的settings.xml的server标签。如下：
```xml
<server>
    <id>user-snapshot</id>
    <username>your name</username>
    <password>your pass</password>
</server>
<server>
    <id>user-release</id>
    <username>your name</username>
    <password>your pass</password>
</server>
```
- 执行maven的deploy命令
    
# TODO
- Session的一级缓存，二级缓存  
- 动态更新权限，刷新缓存
- 同一账号多处登录问题
- 账户踢出接口
- [Shiro其他配置](http://shiro.apache.org/spring-boot.html#configuration-properties )   