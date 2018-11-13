package com.springboot.common.exception;

/**
 * @program: web
 * @description: 业务异常
 * @author: Leslie
 * @create: 2018-07-16 11:19
 **/
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -3321506108371950855L;

    public BusinessException(String msg){
        super(msg);
    }
}
