package com.springboot.web.controller;

import com.springboot.common.base.PublicResult;
import com.springboot.common.base.PublicResultConstant;
import com.springboot.web.annotation.Pass;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: parent
 * @description: base
 * @author: Leslie
 * @create: 2018-11-08 16:48
 **/
@RestController
public class BaseController {


    @GetMapping(value = "/api/401")
    @Pass
    public PublicResult<String> response401(){
        return new PublicResult<String>(PublicResultConstant.UNAUTHORIZED,"请登录");
    }
}
