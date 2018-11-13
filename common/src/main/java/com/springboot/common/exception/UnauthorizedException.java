package com.springboot.common.exception;

/**
 * @program: web
 * @description: 身份认证异常
 * @author: Leslie
 * @create: 2018-07-16 11:05
 **/
public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = -6766585172024224390L;

    public UnauthorizedException(){
        super();
    }
    public UnauthorizedException(String message){
        super(message);
    }

}
