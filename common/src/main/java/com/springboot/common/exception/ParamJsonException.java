package com.springboot.common.exception;

/**
 * @program: web
 * @description: json请求参数异常
 * @author: Leslie
 * @create: 2018-07-16 11:02
 **/
public class ParamJsonException extends RuntimeException {


    private static final long serialVersionUID = -7756133825502381201L;
    private String message;

    @Override
    public String getMessage() {
        return message;
    }
    public ParamJsonException(){

    }
    public ParamJsonException(String message){
        super(message);
        this.message=message;
    }
}
