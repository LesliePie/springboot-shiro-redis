package com.springboot.common.base;

/**
 * @program: web
 * @description:
 * @author: Leslie
 * @create: 2018-07-16 11:28
 **/
public class PublicResult<T> extends BaseResult<T> {
    public static final String DEFAULT_CODE = "90000003";
    public PublicResult(PublicResultConstant publicResultConstant, T data) {
        super(publicResultConstant.getResult(), publicResultConstant.getMsg(), data);
    }
    public PublicResult(String message, T data) {
        super(DEFAULT_CODE, message, data);
    }
}
