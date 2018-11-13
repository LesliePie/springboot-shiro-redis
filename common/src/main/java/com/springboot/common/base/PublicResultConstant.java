package com.springboot.common.base;

/**
 * @program: web
 * @description:
 * @author: Leslie
 * @create: 2018-07-16 11:29
 **/
public enum  PublicResultConstant {
    /**
     * 异常
     */
    FAILED("999999", "系统错误"),
    /**
     * 成功
     */
    SUCCESS("000000", "success"),
    /**
     * 未登录/token过期
     */
    UNAUTHORIZED("000002", "获取登录用户信息失败"),
    PASSWORD_NOT_CORRECT("000003","用户名或密码错误"),
    /**
     * 失败
     */
    ERROR("000001", "操作失败"),
    /**
     * 失败
     */
    PARAM_ERROR("000004", "参数错误"),

    UPLOAD_ERROR("000005","上传文件错误"),
    /**
     *
     */
    INVALID_RE_PASSWORD("000006", "两次输入密码不一致"),
    /**
     * 用户名或密码错误
     */
    INVALID_PASSWORD("000007", "旧密码错误"),
    /**
     * 用户名重复
     */
    USERNAME_ALREADY_IN("000008", "用户已存在"),
    IMAGE_ERROR("000009","验证码生成失败"),
    /**
     * 用户不存在
     */
    INVALID_USER("000010", "用户不存在"),
    /**
     * 参数错误
     */
    INVALID_PARAM_EMPTY("000011", "请求参数为空"),
    /**
     * 校验码错误
     */
    VERIFY_PARAM_ERROR("000012", "校验码错误"),
    /*
     * 校验码过期
     */
    VERIFY_PARAM_PASS("000013", "校验码过期"),

    MOBILE_ERROR("000014","手机号格式错误") ,

    EMAIL_ERROR("000015","邮箱格式错误") ,
    /**
     * 数据更新或增加失败
     */
    DATA_ERROR("000016","数据操作错误"),

    LOGIN_ERROR("000017","登录名格式错误"),
    NOT_FOUND("没有此接口","404"),
    /**
     * 手机号参数异常
     */
    PHONE_NUM_EXTIS("00001","该手机号已存在"),
    INTERNAL_SERVER_ERROR("500","系统错误");

    public String result;
    public String msg;

    PublicResultConstant(String result, String msg) {
        this.result = result;
        this.msg = msg;
    }

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
}
