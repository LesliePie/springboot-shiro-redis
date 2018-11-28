package com.springboot.common.utill;


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.tools.ant.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Leslie on 2018/3/8.
 *
 * @author: Leslie
 * @TIME:10:12
 * @Date:2018/3/8 Description:阿里短信
 */
public class SmsUtil {
    private static Logger logger= LoggerFactory.getLogger(SmsUtil.class);
    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    static final String product = "Dysmsapi";
    /**
     *  产品域名,开发者无需替换
     */
    static final String domain = "dysmsapi.aliyuncs.com";


    static final String accessKeyId = "xxxxxxxxxxxxxxxx";

   static final String accessKeySecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    /**
     * 注册短信模板
     */
    public static final String REGISTER_TEMPLATE="SMS_140835086";
    /**
     * 登录短信模板
     */
    public static final String LOGIN_TEMPLATE="SMS_140835088";
    /**
     * 修改密码验证码
     */
    public static final String UPDATE_PASSWORD_TEMPLATE="SMS_140835085";
    /**
     * 修改绑定的手机号码
     */
    public static final String UPDATE_USER_PHONE="SMS_142945803";
   /**
    *@author: Leslie
    *@Date 2018/3/8 10:36
    *@param: [phoneNum, code]
    *@return com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse
    *@throws
    *@Description: 短信发送
    */
    public  static Boolean   sendMessage(String phoneNum,String code,String template) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNum);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("签名");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(template);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        StringBuilder stringBuilder=new StringBuilder("{\"code\":\"");
        stringBuilder.append(code);
        stringBuilder.append("\"}");
        request.setTemplateParam(stringBuilder.toString());
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        String result=sendSmsResponse.getCode();
        logger.debug("短信发送回执,{}",sendSmsResponse.getMessage());
        return "OK".equals(result);
    }
    /**
     *@author: Leslie
     *@Date 2018/3/8 10:36
     *@param: [bizId]
     *@return com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse
     *@throws
     *@Description: 查询短信
     */
    public static QuerySendDetailsResponse queryResponse(String bizId) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("15000000000");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        request.setSendDate(DateUtils.format(new Date(),"yyyyMMdd"));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);
        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
        return querySendDetailsResponse;
    }
    /**
     *@author: Leslie
     *@Date 2018/3/8 10:23
     *@param: []
     *@return java.lang.String
     *@throws
     *@Description:随机6位数验证码
     */
    public static String getCode(){
        int code=(int)(Math.random()*9000+100000);
        return code + "";
    }
}
