package com.springboot.web.config.shiro;

import com.springboot.common.base.Constant;
import com.springboot.common.base.PublicResult;
import com.springboot.common.base.PublicResultConstant;
import com.springboot.common.utill.JWTUtill;
import com.springboot.model.po.User;
import com.springboot.service.ICacheService;
import com.springboot.service.IUserService;
import com.springboot.service.RedisUtil;
import com.springboot.web.config.other.SpringContextBean;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: web
 * @description: jwt token
 * @author: Leslie
 * @create: 2018-07-16 09:34
 **/
public class JwtFilter extends BasicHttpAuthenticationFilter {
    private Logger logger=LoggerFactory.getLogger(JwtFilter.class);

    private ICacheService cacheService;
    private RedisUtil redisUtil;

    /**
     * Shiro预定义的AuthenticationFilter::isAccessAllowed 是否允许访问 返回 true 表示允许；
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        return subject.isAuthenticated();
    }
    /**
     * @Description: 判断用户是否需要接入,检测header里面是否有Authorization字段
     * @Param: [request, response, mappedValue]
     * @return: boolean
     * @Author: Leslie
     * @Date: 2018/7/16
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest  request, ServletResponse  response) {
        HttpServletRequest req= (HttpServletRequest) request;
        String authorization=req.getHeader("Authorization");
        return authorization!=null;
    }

    /**
     * 执行登陆
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest req= (HttpServletRequest) request;
        String authorization=req.getHeader("Authorization");
        JwtToken jwtToken=new JwtToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        try {
            getSubject(request,response).login(jwtToken);
        }catch (AuthenticationException e){
            response401(request,response);
            return false;
        }
        // 如果没有抛出异常则代表登入成功，返回true
        setUserBean(request,response,jwtToken);
        return true;
    }
    /**
    * @Description: 请求预处理
    * @Param: [request, response]
    * @return: boolean true 继续过滤器链，false不进行处理了
    * @Author: Leslie
    * @Date: 2018/7/16
    */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req= (HttpServletRequest) request;
        HttpServletResponse rep= (HttpServletResponse) response;
        rep.setHeader("Access-Control-Allow-Credentials", "true");
        rep.setHeader("Access-Control-Allow-Origin", "*");
        rep.setHeader("Access-Control-Allow-Methods", "*");
        rep.setHeader("Access-Control-Max-Age", "3600");
        rep.setHeader("Allow", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        rep.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        rep.setCharacterEncoding("utf-8");
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())){
            rep.setStatus(HttpStatus.OK.value());
            return  false;
        }
        //获取token
        String authorization=req.getHeader("Authorization");
        if(StringUtils.isEmpty(authorization)){
            Cookie[] cookies = req.getCookies();
            if(cookies != null){
                for (Cookie cookie : cookies) {
                    if("Authorization".equals(cookie.getName())){
                        authorization = cookie.getValue();
                        break;
                    }
                }
            }
        }
        //检验方法是否需要token认证
        if (verificationPassAnnotation(request,response,req,authorization)){
            return true;
        }
        //验证token是否存在
        if (StringUtils.isBlank(authorization)){
            response401(request,response);
            return false;
        }
        return super.preHandle(request, response);
    }
    /**
    * @Description: 跳转页面至401
    * @Param: [request, response]
    * @return: void
    * @Author: Leslie
    * @Date: 2018/7/16
    */
    private void response401(ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        PublicResult<String> ss = new PublicResult<>(PublicResultConstant.UNAUTHORIZED,"请登录");
        PrintWriter writer=null;
        try {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setHeader("Content-type", "application/json;charset=UTF-8");
            writer= httpServletResponse.getWriter();
            writer.write(JSONObject.fromObject(ss).toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }finally {
            if (writer!=null){
                writer.close();
            }
        }
    }
    /**
    * @Description: 验证方法是否有@Pass注解,有就直接进行放行
    * @Param: [request, response, req, authorization]
    * @return: boolean
    * @Author: Leslie
    * @Date: 2018/7/16
    */
    private boolean verificationPassAnnotation(ServletRequest request, ServletResponse response, HttpServletRequest req, String authorization) throws Exception {
       //contains 验证次方法是否存在，路径方法则不行
        StringBuffer stringBuffer=new StringBuffer(req.getServletPath());
        stringBuffer.append(":--:");
        stringBuffer.append(req.getMethod());
        if (Constant.METHOD_URL_SET.contains(stringBuffer.toString())){
            setCurtentUser(request,response,req,authorization);
            return true;
        }
        for (String url:Constant.METHOD_URL_SET){
            String[] split=url.split(":--:");
            //验证特殊路径方法
            if (StringUtils.countMatches(url,"{")>0&&
                    StringUtils.countMatches(url,"/")==StringUtils.countMatches(split[0],"/")
                    &&(split[1].equals(req.getMethod())||split[1].equals("RequestMapping"))){
                if (isSameUrl(split[0],req.getServletPath())){
                    setCurtentUser(request,response,req,authorization);
                    return true;
                }
            }
        }
        return false;
    }
    /** 
    * @Description: 判断路径参数的url是否和controller方法url一致
    * @Param: [localUrl, reqUrl] 
    * @return: boolean 
    * @Author: Leslie
    * @Date: 2018/7/16 
    */

    private boolean isSameUrl(String localUrl,String reqUrl){
        String[] tmplocal=localUrl.split("/");
        String[] tmpReq=reqUrl.split("/");
        if (tmplocal.length!=tmpReq.length){
            return false;
        }
        StringBuilder localsb=new StringBuilder();
        StringBuilder reqsb=new StringBuilder();
        for(int i=0;i< tmplocal.length;i++){
            if(StringUtils.countMatches(tmplocal[i], "{") > 0){
                tmplocal[i]="*";
                tmpReq[i]="*";
            }
            localsb.append(tmplocal[i]+"/");
            reqsb.append(tmpReq[i]+"/");
        }
        return localsb.toString().trim().equals(reqsb.toString().trim());
    }



    @SuppressWarnings("unchecked")
    private void setUserBean(ServletRequest request, ServletResponse response, JwtToken jwtToken) {
        if (this.cacheService ==null){
            this.cacheService =SpringContextBean.getBean(ICacheService.class);
        }
        //用户作为主体
        /*Map<String,Object> user= (Map<String, Object>) jwtToken.getPrincipal();*/
        Long userId=JWTUtill.getUserNo(jwtToken.getCredentials().toString());
        User user= cacheService.getCacheUser(userId);
        request.setAttribute("currentUser",user);
    }

    /**
     * 根据token是否有效来获取用户
     * @param request
     * @param response
     * @param req
     * @param authorization
     */
    private void setCurtentUser(ServletRequest request,ServletResponse response,HttpServletRequest req,String authorization){
        //如果没有token，因为某些地方不登录也能访问，new一个user
        if (StringUtils.isBlank(authorization)){
            req.setAttribute("currentUser",new User());
        }else {
            //如果能验证user身份
            JwtToken jwtToken=new JwtToken(authorization);
            try {
                //执行登陆
                getSubject(request,response).login(jwtToken);
                // 如果没有抛出异常则代表登入成功，返回true
                setUserBean(request,response,jwtToken);
            }catch (AuthenticationException e){
                //假如用户token是失效的，避免影响用户相关浏览，当做没有token
                req.setAttribute("currentUser",new User());
            }
        }
    }
}
