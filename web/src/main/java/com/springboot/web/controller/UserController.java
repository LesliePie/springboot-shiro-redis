package com.springboot.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.springboot.common.base.PublicResult;
import com.springboot.common.base.PublicResultConstant;
import com.springboot.model.po.User;
import com.springboot.model.vo.UserVo;
import com.springboot.service.IUserService;
import com.springboot.web.annotation.Log;
import com.springboot.web.annotation.Pass;
import com.springboot.web.annotation.ValidationParam;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @program: parent
 * @description: 用户接口
 * @author: Leslie
 * @create: 2018-11-07 16:20
 **/
@RestController
@Api(value = "用户相关接口")
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private IUserService userService;

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    @Log(description = "测试接口")
    public PublicResult<UserVo> user(@PathVariable Long id){
        UserVo userVo= userService.getUserById(id);
        return new PublicResult<UserVo>(PublicResultConstant.SUCCESS,userVo);
    }

    /**
     * 用户注册
     * @param jsonObject
     * @return
     */
    @PostMapping
    @Log(description = "用户注册")
    @Pass
    public  PublicResult<UserVo> register(@RequestBody@ValidationParam(value = "password,repassword,login") JSONObject jsonObject){
        String password=jsonObject.getString("password");
        String rePassword=jsonObject.getString("repassword");
        String login=jsonObject.getString("login");
        UserVo vo= userService.registerUser(login,password,rePassword);
        return new PublicResult<>(PublicResultConstant.SUCCESS,vo);
    }

    /**
     * 用户登录接口
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/login")
    @Log(description = "用户登录接口")
    @Pass
    public PublicResult<UserVo> login(@RequestBody@ValidationParam(value ="password,login" )JSONObject jsonObject){
        String pass=jsonObject.getString("password");
        String login=jsonObject.getString("login");
        UserVo userVo=userService.login(login,pass);
        return new PublicResult<>(PublicResultConstant.SUCCESS,userVo);
    }

}
