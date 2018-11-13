package com.springboot.common.base;

/**
 * @program: web
 * @description: 基础json返回
 * @author: Leslie
 * @create: 2018-07-16 11:27
 **/
public class BaseResult<T> {
    private String result;
    private String msg;
    private T data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseResult() {
    }

    public BaseResult(String result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public BaseResult(String result, String msg, T data) {
        this.result = result;
        this.msg = msg;
        this.data = data;
    }
}
