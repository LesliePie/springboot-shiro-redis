# springboot-shiro-redis
springboot集成shiro,mybatisPlus 等快速开发框架
# 简介
    *采用redis作为缓存，采用jwt进行鉴权，多数据源，redis可以实现多个redisTemplate而达到不同类型缓存放入不同 index库中
    *[Mybatis-Plus](https://github.com/baomidou/mybatis-plus)是一个 [Mybatis](http://www.mybatis.org/mybatis-3/) 的增强工具，有代码生成器，并且提供了类似hibernate的单表CRUD操作，又保留了mybatis的特性支持定制化 SQL，专注于业务开发
    *Apache Shiro 作为安全框架
    *swagger-ui作为文档插件，自动生成相关文档
# 功能简介
     1.@pass注解：无需进行登录认证，若请求头带上token，token验证成功则会给出处于登录状态应后返回的数据，token验证错误不予报错，只是返回未登录情况下的数据。
     2.@ValidationParam注解：当采用jsonObject传参，验证必填参数
     3.用springAop 声明式事务      
     4.使用bcrypt算法加密密码
 ## 逻辑
     1.post 访问登录接口，成功返回token,并将token存入redis，失败则返回错误信息
     2.需要登录的接口请求头Headers中添加Authorization和登录时返回的token令牌
     3.服务端先进行token认证，在进行业务数据返回
##其他 
     个人第一次编写项目，希望各位大佬指出错误        