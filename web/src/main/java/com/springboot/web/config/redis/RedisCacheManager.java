package com.springboot.web.config.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @program: education-parent
 * @description: shiro缓存
 * @author: Leslie
 * @create: 2018-08-06 17:34
 **/
public class RedisCacheManager implements CacheManager,Destroyable {
    @Autowired
    @SuppressWarnings("rawtypes")
    private RedisTemplate<String, String> redisTemplate;


    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new RedisCache<>(name, redisTemplate);
    }


    @Override
    public void destroy() throws Exception {
        redisTemplate=null;
    }

}
