package com.springboot.common.utill;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @program: web
 * @description: jwt验证工具
 * @author: Leslie
 * @create: 2018-07-16 17:13
 **/
public class JWTUtill {
    /**
     * 过期时间 15天
     */
    private static final long EXPIRE_TIME=15*24*60*60*1000;
    /**
    * @Description: 校验token是否正确
    * @Param: [token 密钥, userId, secret 密码]
    * @return: boolean
    * @Author: Leslie
    * @Date: 2018/7/16
    */
    public static boolean verity(String token,Long userId,String secret){
        try {
            Algorithm algorithm=Algorithm.HMAC256(secret);
            JWTVerifier verifier= JWT.require(algorithm)
                        .withClaim("userId",userId)
                        .build();
            verifier.verify(token);
            return true;
        }catch (Exception exception){
            return false;
        }
    }



    /** 
    * @Description: 获取token中的信息，无需secret也可以解密
    * @Param: [token] 
    * @return: java.lang.Long 
    * @Author: Leslie
    * @Date: 2018/7/16 
    */ 
    public static Long getUserNo(String token){
        try {
            DecodedJWT  jwt=JWT.decode(token);
            return jwt.getClaim("userId").asLong();
        }catch (JWTDecodeException e){
            return null;
        }
    }



    /** 
    * @Description: 生成签名，指定时间后失效
    * @Param: [userId 用户唯一识别号, secret 密码]
    * @return: java.lang.String  加密后token
    * @Author: Leslie
    * @Date: 2018/7/16 
    */ 
    public static  String sign(Long userId,String secret){
        try {
            Date date=new Date(System.currentTimeMillis()+EXPIRE_TIME);
            Algorithm algorithm=Algorithm.HMAC256(secret);
            return JWT.create()
                    .withClaim("userId",userId)
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (UnsupportedEncodingException e){
            return null;
        }
    }




}
