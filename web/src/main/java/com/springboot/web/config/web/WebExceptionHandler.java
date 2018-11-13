package com.springboot.web.config.web;

import com.alibaba.fastjson.JSONException;
import com.fasterxml.jackson.core.JsonParseException;
import com.springboot.common.base.BaseResult;
import com.springboot.common.base.PublicResult;
import com.springboot.common.base.PublicResultConstant;
import com.springboot.common.exception.BusinessException;
import com.springboot.common.exception.FileUploadException;
import com.springboot.common.exception.ParamJsonException;
import com.springboot.common.exception.UnauthorizedException;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.exceptions.TemplateInputException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

/**
 * @program: web
 * @description: controller异常处理
 * @author: Leslie
 * @create: 2018-07-16 11:20
 **/
@ControllerAdvice
public class WebExceptionHandler {

    private static Logger logger=LoggerFactory.getLogger(WebExceptionHandler.class);
    /** 
    * @Description: 在requestMapping前初始化数据绑定器
    * @Param: [binder] 
    * @return: void 
    * @Author: Leslie
    * @Date: 2018/7/16 
    */
    @InitBinder
    public void  initBinder(WebDataBinder binder){

    }
    /** 
    * @Description: 将值绑定到Model，全局RequestMapping可以获取该值
    * @Param: [model] 
    * @return: void 
    * @Author: Leslie
    * @Date: 2018/7/16 
    */ 
    @ModelAttribute
    public void  addAttribute(Model model){
        
    }
    /**
    * @Description: 全局异常处理
    * @Param: [e]
    * @return: PublicResult<java.lang.String>
    * @Author: Leslie
    * @Date: 2018/7/16
    */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public PublicResult<String> errorHandler(Exception e){
        e.printStackTrace();
        logger.error("接口出现严重异常：{}",e.getMessage());
        return new PublicResult<>(PublicResultConstant.FAILED,null);
    }
    /** 
    * @Description: 用户认证和shiro异常处理
    * @Param: [] 
    * @return: PublicResult<java.lang.String>
    * @Author: Leslie
    * @Date: 2018/7/16 
    */ 
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({UnauthorizedException.class,ShiroException.class,TemplateInputException.class})
    public PublicResult<String> authorizedExceptionHandler(){
        return new PublicResult<>(PublicResultConstant.PASSWORD_NOT_CORRECT,null);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(UnknownAccountException.class)
    public PublicResult<String> unknownAccountExceptionHandler(){
        return new PublicResult<>(PublicResultConstant.UNAUTHORIZED,null);
    }


    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public  PublicResult<String> handleApiConstraintViolationException(ConstraintViolationException ex) {
        String message = "";
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            message += violation.getMessage() + ", ";
        }
        return new PublicResult<>(PublicResultConstant.ERROR,message);
    }
    /**
    * @Description: 业务逻辑异常处理
    * @Param: [e]
    * @return: BaseResult
    * @Author: Leslie
    * @Date: 2018/7/16
    */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BusinessException.class)
    public BaseResult bussinesExceptionHandler(BusinessException e){
        if (e instanceof BusinessException){
            logger.info("数据操作失败，失败原因为{}",e.getMessage());
            return new BaseResult(PublicResultConstant.DATA_ERROR.result,e.getMessage());
        }
        return new PublicResult<String>(PublicResultConstant.ERROR,null);
    }
    /**
    * @Description: 自定义参数异常处理
    * @Param: [e]
    * @return: BaseResult
    * @Author: Leslie
    * @Date: 2018/7/16
    */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ParamJsonException.class)
    public BaseResult paramJsonExceptionHandler(ParamJsonException e){
        if (e instanceof ParamJsonException){
            logger.info("请求参数错误,错误内容为{}",e.getMessage());
            return new BaseResult(PublicResultConstant.PARAM_ERROR.result,e.getMessage());
        }
        return new PublicResult<String>(PublicResultConstant.ERROR,null);
    }

    /**
     * JSonObject错误
     * @param e
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(JSONException.class)
    public BaseResult fastJsonException(JSONException e){
        if (e instanceof JSONException){
            logger.info("请求参数错误,错误内容为{}",e.getMessage());
            return new BaseResult(PublicResultConstant.PARAM_ERROR.result,e.getMessage());
        }
        return new PublicResult<String>(PublicResultConstant.ERROR,null);
    }
    /**
     * Valid 参数校验异常捕获
     * @param e
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({MethodArgumentNotValidException.class,JsonParseException.class})
    public BaseResult paramJsonExceptionHandler(MethodArgumentNotValidException e){
        if (e instanceof MethodArgumentNotValidException){
            logger.info("请求参数错误,错误内容为{}",e.getBindingResult().getFieldError().getDefaultMessage());
            List<ObjectError> errors=e.getBindingResult().getAllErrors();
            StringBuilder stringBuilde=new StringBuilder("参数异常:");
            errors.forEach(item->{
                stringBuilde.append(item.getObjectName()).append(":");
                stringBuilde.append(item.getDefaultMessage());
                stringBuilde.append(",");
            });
            return new BaseResult(PublicResultConstant.PARAM_ERROR.result,stringBuilde.substring(0,stringBuilde.length()-1));
        }
        return new PublicResult<String>(PublicResultConstant.PARAM_ERROR,null);
    }
    /**
    * @Description: 上传文件错误处理
    * @Param: [e]
    * @return: com.education.base.BaseResult
    * @Author: Leslie
    * @Date: 2018/7/19
    */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(FileUploadException.class)
    public BaseResult fileUploadExceptionHandler(FileUploadException e){
            logger.info("文件上传错误，错误内容为{}",e);
            return new BaseResult(PublicResultConstant.UPLOAD_ERROR.result,e.getMessage());
    }
}
