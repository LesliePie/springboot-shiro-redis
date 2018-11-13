package com.springboot.web.config.shiro;

import com.springboot.common.utill.JWTUtill;
import com.springboot.common.utill.StringUtils;
import com.springboot.model.po.User;
import com.springboot.service.ICacheService;
import com.springboot.service.RedisUtil;
import com.springboot.web.config.other.SpringContextBean;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.Map;

/**
 * @program: web
 * @description: 自定义shiro实现
 * @author: Leslie
 * @create: 2018-07-16 17:36
 **/
public class UserRealm extends AuthorizingRealm {
    private ICacheService cacheService;
    private RedisUtil redisUtil;
    /** 
    * @Description: 重写此方法
    * @Param: [token] 
    * @return: boolean 
    * @Author: Leslie
    * @Date: 2018/7/16 
    */ 
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
    * @Description: 用户角色资源控制
    * @Param: [principalCollection]
    * @return: org.apache.shiro.authz.AuthorizationInfo
    * @Author: Leslie
    * @Date: 2018/7/16
    */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }
    /** 
    * @Description: 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
    * @Param: [authenticationToken] 
    * @return: org.apache.shiro.authc.AuthenticationInfo 
    * @Author: Leslie
    * @Date: 2018/7/16 
    */ 
    @Override
    @SuppressWarnings(value = "unchecked")
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (redisUtil==null){
            this.redisUtil=SpringContextBean.getBean(RedisUtil.class);
        }
        //获取token
        String token= (String) authenticationToken.getCredentials();
        if (StringUtils.isBlank(token) ||!redisUtil.sHasKey("token",token)){
            throw new UnknownAccountException("token invalid");
        }
        return new SimpleAuthenticationInfo(token,token,this.getName());
    }

    /**
     * 登出方法
     * @param principals
     */
    @Override
    @SuppressWarnings(value = "unchecked")
    public void onLogout(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
        Map<String,Object> map  = (Map<String, Object>) principals.getPrimaryPrincipal();
        String token= map.get("token").toString();
        if (redisUtil==null){
            this.redisUtil=SpringContextBean.getBean(RedisUtil.class);
        }
        redisUtil.setRemove("token",token);
        removeUserCache(token);
    }

    /**
     * 清除用户缓存
     *
     * @param token
     */
    public void removeUserCache(String token) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection();
        principals.add(token, super.getName());
        super.clearCachedAuthenticationInfo(principals);
    }

    /**
     * shiro的密码验证方法，此处我们改为验证用户
     * @param token
     * @param info
     * @throws AuthenticationException
     */
    @Override
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info)
            throws AuthenticationException {
        if (cacheService==null){
            this.cacheService=SpringContextBean.getBean(ICacheService.class);
        }
        Long userId=JWTUtill.getUserNo(token.getCredentials().toString());
        if (userId==null){
            throw new UnknownAccountException("token invalid");
        }
        //验证用户
        User userBean=cacheService.getCacheUser(userId);
        if (userBean==null){
            throw new UnknownAccountException("User didn't existed! ");
        }
        super.assertCredentialsMatch(token, info);
    }



}
