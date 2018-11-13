package com.springboot.web.aspect;

import com.alibaba.fastjson.JSONObject;
import com.springboot.common.base.PublicResultConstant;
import com.springboot.common.exception.ParamJsonException;
import com.springboot.common.utill.StringUtils;
import com.springboot.web.annotation.ValidationParam;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @program: web
 * @description:
 * @author: Leslie
 * @create: 2018-07-16 15:41
 **/
public class ValidationParamAspect implements AspectApi {
    @Override
    public Object doHandlerAspect(Object[] objects, ProceedingJoinPoint point, Method method, boolean isAll) {
        String validationParamValue=StringUtils.getMethodAnnotationOne(method,ValidationParam.class.getSimpleName());
        if (StringUtils.isNotBlank(validationParamValue)){
            for (int i=0;i<objects.length;i++){
                if(objects[i] instanceof JSONObject){
                    JSONObject jsonObject = JSONObject.parseObject(objects[i].toString());
                    //是否有所有必须参数
                    hasAllRequired(jsonObject,validationParamValue);
                }else {
                    continue;
                }
            }
        }
        return objects;
    }




    private void hasAllRequired(final JSONObject jsonObject, String requiredColumns) {
        if (StringUtils.isNotBlank(requiredColumns)){
            String[] colums=requiredColumns.split(",");
            String missCol="";
            for (String colum:colums){
                Object val=jsonObject.get(colum.trim());
                if (Objects.isNull(val)){
                    missCol +=colum+" ";
                }else {
                    if ("phone".equals(colum)&&!StringUtils.checkMobileNumber(val.toString())){
                            throw new ParamJsonException(PublicResultConstant.MOBILE_ERROR.msg);
                    }
                    if ("email".equals(colum)&&!StringUtils.checkEmail(val.toString())){
                            throw new ParamJsonException(PublicResultConstant.EMAIL_ERROR.msg);
                    }
                }
            }
            if (StringUtils.isNotBlank(missCol)){
                jsonObject.clear();
                throw new ParamJsonException("缺少必填参数："+missCol.trim());
            }
        }
    }
}
