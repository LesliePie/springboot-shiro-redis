package com.springboot.model.vo;

import com.springboot.model.po.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @program: parent
 * @description: 用户Vo
 * @author: Leslie
 * @create: 2018-11-08 14:17
 **/
@ApiModel(value = "用户信息")
public class UserVo extends User {
    private static final long serialVersionUID = 5510133162963911546L;
    /**
     * 用户token
     */
    @ApiModelProperty(value = "用户token")
    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
