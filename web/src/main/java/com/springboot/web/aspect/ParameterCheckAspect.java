package com.springboot.web.aspect;

import com.alibaba.fastjson.JSONObject;
import com.springboot.common.base.BaseResult;
import com.springboot.common.base.PageResult;
import com.springboot.common.utill.StringUtils;
import com.springboot.model.vo.UserVo;
import com.springboot.web.annotation.Log;
import com.springboot.web.annotation.ParamXssPass;
import com.springboot.web.annotation.ValidationParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program: web
 * @description: 切面:防止xss攻击 记录log  参数验证
 * @author: Leslie
 * @create: 2018-07-16 14:58
 **/
@Configuration
@Aspect
public class ParameterCheckAspect {

    /**
    * @Description: 防止XSS攻击
    * @Param: []
    * @return: void
    * @Author: Leslie
    * @Date: 2018/7/16
    */
    @Pointcut("execution(* com.springboot.web.controller..*(..))  ")
    public  void preventXss(){

    }
    @Pointcut("@annotation(com.springboot.web.annotation.Log)")
    public void recordLog(){

    }



    @Around(value = "preventXss()||recordLog()")
    public Object validationPoint(ProceedingJoinPoint point)throws Throwable{
        Method method=currentMethod(point,point.getSignature().getName());
        boolean isLogEmpt=Objects.isNull(method.getAnnotation(Log.class));
        boolean isParamXssPassEmpty=Objects.isNull(method.getAnnotation(ParamXssPass.class));
        Object[] agrs=point.getArgs();
        if (isParamXssPassEmpty){
            agrs=handlerRequstParam(point);
        }
        boolean isValidationParamEmpty=StringUtils.isEmpty(StringUtils.getMethodAnnotationOne(method,ValidationParam.class.getSimpleName()));
        if (!isLogEmpt&&isValidationParamEmpty){
            AspectHandler aspectHandler=new RecordLogOperate();
            return aspectHandler.doAspectHandler(point,agrs,method,false);
        }
        if (!isValidationParamEmpty&&!isLogEmpt){
            AspectHandler aspectHandler=new RecordLogOperate();
            return aspectHandler.doAspectHandler(point,agrs,method,true);
        }
        if(!isValidationParamEmpty && isLogEmpt){
            AspectHandler aspectHandler = new ValidationParamOperate();
            aspectHandler.doAspectHandler(point,agrs,method,false);
        }
        return  point.proceed(agrs);
    }
    /**
    * @Description: 隐藏返回的密码
    * @Param: [point]
    * @return: java.lang.Object
    * @Author: Leslie
    * @Date: 2018/7/16
    */
    @Around( "execution(* com.springboot.web.controller..*(..) )" )
    public Object returnValueHandle (ProceedingJoinPoint point)throws Throwable{
        Object retrunValue=point.proceed();
        if (retrunValue instanceof BaseResult){
            BaseResult baseResult= (BaseResult) retrunValue;
            Object data=baseResult.getData();
            if (data instanceof PageResult){
                PageResult pageResult= (PageResult) data;
                List list=  pageResult.getList();
                doPasswd2NullByList(list);
                pageResult.setList(list);
            }
            if (data instanceof UserVo){
                UserVo user= (UserVo) data;
                user.setPassword(null);
                baseResult.setData(user);
            }
            if(data instanceof Map){
                Map<String,Object> map = (Map) data;
                doPasswd2NullByMap(map);
                baseResult.setData(map);
            }
            if(data instanceof List){
                List list = (List) data;
                doPasswd2NullByList(list);
                baseResult.setData(list);
            }
        }
        return retrunValue;
    }


    private Object[] handlerRequstParam(ProceedingJoinPoint point) {
        Object[] args=point.getArgs();
        for (int i=0;i<args.length;i++){
            if (args[i] instanceof JSONObject){
                args[i]=JSONObject.parseObject(xssEncode(args[i].toString()));
            }else if(args[i] instanceof String){
                args[i]=xssEncode(args[i].toString());
            }else {
                continue;
            }
        }
        return args;
    }
    private void doPasswd2NullByList(List list) {
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        for (int i = 0; i <list.size() ; i++) {
            if(list.get(i) instanceof UserVo){
                UserVo user = (UserVo) list.get(i);
                user.setPassword(null);
                list.set(i,user);
            }
            if(list.get(i) instanceof Map){
                Map<String,Object> map = (Map) list.get(i);
                doPasswd2NullByMap(map);
                list.set(i,map);
            }
        }
    }

    private void doPasswd2NullByMap(Map<String, Object> map) {
        for (String key: map.keySet()) {
            Object obj = map.get(key);
            if(obj instanceof UserVo){
                UserVo user = (UserVo) obj;
                user.setPassword(null);
                map.put(key,user);
            }
        }
    }


    private String xssEncode(String s) {
        if (s == null || "".equals(s)) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '>':
                    //全角大于号
                    sb.append("&gt;");
                    break;
                case '<':
                    //全角小于号
                    sb.append("&lt;");
                    break;
                case '\'':
                    //全角单引号
                    sb.append('‘');
                    break;
                case '&':
                    //全角
                    sb.append('＆');
                    break;
                case '\\':
                    //全角斜线
                    sb.append('＼');
                    break;
                case '#':
                    //全角井号
                    sb.append('＃');
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /** 
    * @Description: 获取目标类的所有方法，找到当前要执行的方法
    * @Param: [point, name] 
    * @return: java.lang.reflect.Method 
    * @Author: Leslie
    * @Date: 2018/7/16 
    */ 
    private Method currentMethod(ProceedingJoinPoint point, String name) {
        Method[] methods  = point.getTarget().getClass().getMethods();
        Method resultMethod=null;
        for (Method method:methods){
            if (method.getName().equals(name)){
                resultMethod=method;
                break;
            }
        }
        return resultMethod;
    }
}
