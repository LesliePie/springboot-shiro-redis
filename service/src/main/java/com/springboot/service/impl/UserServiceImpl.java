package com.springboot.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.springboot.common.exception.BusinessException;
import com.springboot.common.utill.JWTUtill;
import com.springboot.mapper.UserMapper;
import com.springboot.model.po.User;
import com.springboot.model.vo.UserVo;
import com.springboot.service.ICacheService;
import com.springboot.service.IUserService;
import com.springboot.service.RedisUtil;
import org.dozer.DozerBeanMapper;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: parent
 * @description:
 * @author: Leslie
 * @create: 2018-11-07 16:10
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private DozerBeanMapper mapper;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public UserVo getUserById(Long id) {
        if (id==null||id<0){
            throw new BusinessException("id非法");
        }
        User user= cacheService.getCacheUser(id);
        UserVo userVo=mapper.map(user,UserVo.class);
        return userVo;
    }

    @Override
    public UserVo registerUser(String login, String password, String rePassword) {
        //查询是否存在该登录账号
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.where("login",login);
        User user=selectOne(wrapper);
        if (user!=null){
            throw new BusinessException("该账号已存在");
        }
        //判断密码
        if (!password.equals(rePassword)){
            throw new BusinessException("两次密码输入不一致");
        }
        User newUser=new User();
        newUser.setLogin(login);
        //密码加密
        String pass=BCrypt.hashpw(password,BCrypt.gensalt(12));
        newUser.setPassword(pass);
        //将用户保存，并存入缓存
        cacheService.insertUser(newUser);
        //token获取
        return signUser(newUser);
    }

    @Override
    public UserVo signUser(User user) {
        String token= JWTUtill.sign(user.getId(),user.getPassword());
        UserVo vo=mapper.map(user,UserVo.class);
        vo.setToken(token);
        //保存用户token
        redisUtil.sSetAndTime("token",24*60*60,token);
        return vo;
    }

    @Override
    public UserVo login(String login, String pass) {
        //是否存在该用户
        EntityWrapper wrapper=new EntityWrapper();
        wrapper.where("login",login);
        User user=selectOne(wrapper);
        if (user==null){
            throw new BusinessException("用户或密码错误");
        }
        if (!BCrypt.checkpw(pass,user.getPassword())){
            throw new BusinessException("用户名或密码错误");
        }
        return signUser(user);
    }
}
