package com.springboot.common.base;

/**
 * @program: education-parent
 * @description: 三方请求接口地址
 * @author: Leslie
 * @create: 2018-10-11 17:53
 **/
public class ThirdPartRequestUrl {
    /**----------------------------------------------------------------------------------微信---------------------------------------------------------------------------------*/
    /**
     * 通过code获取access_token的接口。
     */
    public static final String WEiCHAT_ACCESS_TOKEN_URL ="https://api.weixin.qq.com/sns/oauth2/access_token";
    /**
     * 刷新或续期access_token使用
     */
    public static final String WEICHAT_REFRESH_TOKEN_URL ="https://api.weixin.qq.com/sns/oauth2/refresh_token";
    /**
     * 检验授权凭证（access_token）是否有效
     */
    public static final String WEICHAT_AUTH_URL ="https://api.weixin.qq.com/sns/auth";
    /**
     * 获取用户个人信息（UnionID机制）
     */
    public static final String WEiCHAT_ACCESS_TOKEN_URLUSERINFO_URL ="https://api.weixin.qq.com/sns/userinfo";
    /**----------------------------------------------------------------------------------微博-------------------------------------------------------------------------------------*/
    /**
     * 通过code获取access_token的接口。
     */
    public static final String WEIBO_ACCESS_TOKEN_URL="https://api.weibo.com/oauth2/access_token";
    /**
     * 检验授权凭证（access_token）是否有效
     */
    public static final String WEIBO_AUTH_URL ="https://api.weibo.com/oauth2/get_token_info";
    /**
     * 获取微博用户信息
     */
    public static final String WEIBO_USER_MESSAGE="https://api.weibo.com/2/users/show.json";
    /**
     * 取消微博用户信息绑定
     */
    public static final String WEIBO_USER_CANCEL="https://api.weibo.com/oauth2/revokeoauth2";

}
