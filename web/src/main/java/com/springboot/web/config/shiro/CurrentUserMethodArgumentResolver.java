package com.springboot.web.config.shiro;

import com.springboot.common.exception.UnauthorizedException;
import com.springboot.model.po.User;
import com.springboot.web.annotation.CurrentUser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @program: web
 * @description: @CurrentUser  注解方法注入当前用户
 * @author: Leslie
 * @create: 2018-07-16 10:52
 **/
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //参数是否含有某个类型参数
        boolean type=methodParameter.getParameterType().isAssignableFrom(User.class);
        //参数是否含有某个注解
        boolean annontion=methodParameter.hasParameterAnnotation(CurrentUser.class);
        return type&&annontion;

    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        User user= (User) nativeWebRequest.getAttribute("currentUser",RequestAttributes.SCOPE_REQUEST);
        if (user==null){
            throw new UnauthorizedException("获取用户信息失败");
        }
        return user;
    }
}
