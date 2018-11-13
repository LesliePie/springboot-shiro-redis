package com.springboot.service.impl;

import com.springboot.mapper.UserMapper;
import com.springboot.model.po.User;
import com.springboot.service.ICacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @program: parent
 * @description:
 * @author: Leslie
 * @create: 2018-11-08 14:00
 **/
@Service
public class CacheServiceImpl implements ICacheService {
    @Autowired
    private UserMapper userMapper;

    @Override
    @Cacheable(value = "userCache",condition = "#p0!=null",key = "#root.caches[0].name+':'+#p0")
    public User getCacheUser(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    @CachePut(value = "userCache")
    public void insertUser(User newUser) {
        userMapper.insert(newUser);
    }
}
