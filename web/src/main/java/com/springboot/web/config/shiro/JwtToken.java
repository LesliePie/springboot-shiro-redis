package com.springboot.web.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: web
 * @description: 用于进行shiro的认证的token
 * @author: Leslie
 * @create: 2018-07-16 09:44
 **/
public class JwtToken implements AuthenticationToken {
    /**
     * token作为凭证
     */
    private String token;



    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
