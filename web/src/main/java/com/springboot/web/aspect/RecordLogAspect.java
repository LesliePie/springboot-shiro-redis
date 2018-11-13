package com.springboot.web.aspect;

import com.springboot.common.utill.StringUtils;
import com.springboot.web.annotation.Log;
import net.sf.json.JSONArray;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * @program: web
 * @description:
 * @author: Leslie
 * @create: 2018-07-16 15:34
 **/
public class RecordLogAspect implements AspectApi{
    private Logger logger=LoggerFactory.getLogger(RecordLogAspect.class);
    @Override
    public Object doHandlerAspect(Object[] objects, ProceedingJoinPoint point, Method method, boolean isAll) throws Throwable {
        Log log=method.getAnnotation(Log.class);
        //异常日志信息
        String actionLog=null;
        StackTraceElement[] stackTrace=null;
        //是否执行异常
        boolean isException=false;
        //接受时间戳
        long endTime;
        //开始时间戳
        long staTime=System.currentTimeMillis();
        try {
            if (isAll){
                AspectHandler aspectHandler=new ValidationParamOperate();
                aspectHandler.doAspectHandler(point,objects,method,false);
            }
            return point.proceed(objects);
        }catch (Throwable throwable){
            isException=true;
            actionLog=throwable.getMessage();
            stackTrace=throwable.getStackTrace();
            throw throwable;
        }finally {
            logHandle(point,method,log,actionLog,staTime,isException,stackTrace);
        }
    }

    private void logHandle(ProceedingJoinPoint point, Method method, Log log, String actionLog, long staTime, boolean isException, StackTraceElement[] stackTrace) {
        String strMethodName = point.getSignature().getName();
        String strClassName = point.getTarget().getClass().getName();
        Object[] params = point.getArgs();
        StringBuffer bfParams = new StringBuffer();
        Enumeration<String> paraNames = null;
        HttpServletRequest request = null;
        // 前端登录用户名
        if (params != null && params.length > 0) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            paraNames = request.getParameterNames();
            String key;
            String value;
            while (paraNames.hasMoreElements()) {
                key = paraNames.nextElement();
                value = request.getParameter(key);
                if ("password".equals(key)||"repassword".equals(key)){
                    value="********";
                }
                bfParams.append(key).append("=").append(value).append("&");
            }
            if (StringUtils.isBlank(bfParams)) {
                bfParams.append(request.getQueryString());
            }
            if ((bfParams == null || bfParams.length() < 1 || bfParams.toString().equals("null")) && params != null
                    && params.length > 0) {
                try {
                    bfParams = new StringBuffer();
                    bfParams.append(JSONArray.fromObject(params).toString());
                } catch (Exception e) {

                }
            }
        }
        String strMessage = String.format("[接口]:%s,[类名]:%s,[方法]:%s,[参数]:%s", log.description(), strClassName, strMethodName, bfParams.toString());
        logger.info(strMessage);
        //TODO 切面日志SERvice操作
    }
}
