package com.springboot.service;

import com.baomidou.mybatisplus.service.IService;
import com.springboot.model.po.User;
import com.springboot.model.vo.UserVo;

import java.util.Map;

/**
 * @program: parent
 * @description: userService
 * @author: Leslie
 * @create: 2018-11-07 16:09
 **/
public interface IUserService extends IService<User> {
    /**
     * 通过id查找用户信息
     * @param id 用户id
     * @return
     */
    UserVo getUserById(Long id);

    /**
     * 用户注册
     * @param login 登录号
     * @param password 密码
     * @param rePassword 再次输入密码
     * @return
     */
    UserVo registerUser(String login, String password, String rePassword);

    /**
     * 验签用户，获取token
     * @param user 用户实体
     * @return
     */
    UserVo signUser(User user);

    /**
     * 用户登录
     * @param login 登录
     * @param pass 密码
     * @return
     */
    UserVo login(String login, String pass);
}
