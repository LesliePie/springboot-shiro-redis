package com.springboot.web.config.redis;

import com.springboot.web.config.other.SpringContextBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @program: education-parent
 * @description: springCache管理器
 * @author: Leslie
 * @create: 2018-09-07 15:50
 **/
@Configuration
public class CacheManagerConfig extends CachingConfigurerSupport {
    @Autowired
    @SuppressWarnings("rawtypes")
    private RedisTemplate redisTemplate;
    /**
     * @Description: key的生成策略
     * @Param: []
     * @return: org.springframework.cache.interceptor.KeyGenerator
     * @Author: Leslie
     * @Date: 2018/7/19
     */
    @Override
    @Bean
    public KeyGenerator keyGenerator(){
        return (o, method, objects) -> {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(o.getClass().getName());
            stringBuilder.append(method.getName());
            for (Object object:objects){
                stringBuilder.append(object.toString());
            }
            return stringBuilder.toString();
        };
    }


    @Bean
    @Override
    public CacheManager cacheManager() {
        if (redisTemplate==null){
            this.redisTemplate=SpringContextBean.getBean(RedisTemplate.class);
        }
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        // 设置缓存过期时间
        // redisCacheManager.setDefaultExpiration(60);//秒
        return redisCacheManager;
    }
    /**
     * @Description: 管理redis缓存
     * @Param: [redisTemplate]
     * @return: org.springframework.data.redis.cache.RedisCacheManager
     * @Author: Leslie
     * @Date: 2018/7/19
     */
    @Bean
    public RedisCacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate){
        RedisCacheManager cacheManager=new RedisCacheManager(redisTemplate);
        return cacheManager;
    }

}
