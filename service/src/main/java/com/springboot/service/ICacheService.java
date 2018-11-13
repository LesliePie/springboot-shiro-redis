package com.springboot.service;

import com.springboot.model.po.User;

/**
 * @program: parent
 * @description: 专门调用cache，Spring cache service内部调用无效，因此单独独立出接口
 * @author: Leslie
 * @create: 2018-11-08 13:58
 **/
public interface ICacheService {
    /**
     * 根据id获取缓存中的用户
     * @param userId
     * @return
     */
    User getCacheUser(Long userId);

    /**
     * 保存用户
     * @param newUser
     */
    void insertUser(User newUser);
}
