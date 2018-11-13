package com.springboot.web.config.other;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;

/**
 * @program: web
 * @description: 声明式事务配置
 * @author: Leslie
 * @create: 2018-07-16 14:15
 **/
@Configuration
public class TransactionConfig {
    private static final String   CUSTOMIZE_TRANSACTION_INTERCEPTOR_NAME               = "customizeTransactionInterceptor";
    /**
     * 针对类名为Service或*ServiceImpl的进行
     */
    private static final String[] DEFAULT_TRANSACTION_BEAN_NAMES                       = { "*Service" , "*ServiceImpl" };
    /**
     * REQUIRED 支持事务
     */
    private static final String[] DEFAULT_REQUIRED_METHOD_RULE_TRANSACTION_ATTRIBUTES  = {
            "add*" ,
            "save*" ,
            "insert*" ,
            "delete*" ,
            "update*" ,
            "join*",
            "exit*",
            "edit*" ,
            "batch*" ,
            "create*" ,
            "remove*",
            "lock*",
            "record*",
            "touch*",
            "apply*",
            "modify*",
            "execute*",
            "merge*",
            "put*",
            "sync*",
            "copy*",
            "change*",
            "adjust*",
            "start*",
            "init*",
            "upload*",
            "batch*",
            "has*",
            "publish*",
            "import*",
            "export*",
            "disable*",
            "cancel*",
            "submit*",
            "send*",
            "process*",
            "complete*",
            "hide*",
            "top*",
            "reset*",
            "bind*",
            "handle*",
            "reSubmit*",
            "close*",
            "register*",
            "sign*"
    };
    /**
     * READONLY 只读事务
     */
    private static final String[] DEFAULT_READ_ONLY_METHOD_RULE_TRANSACTION_ATTRIBUTES = {
            "get*" ,
            "count*" ,
            "find*" ,
            "query*" ,
            "select*" ,
            "list*" ,
            "search*",
            "is*",
            "verify*",
            "check*",
            "read*",
            "*"
    };
    /**
     * 自定义事务 BeanName 拦截
     */
    private              String[] customizeTransactionBeanNames                        = {};
    /**
     * 自定义方法名的事务属性相关联,可以使用通配符(*)字符关联相同的事务属性的设置方法; 只读事务
     */
    private              String[] customizeReadOnlyMethodRuleTransactionAttributes     = {};
    /**
     * 自定义方法名的事务属性相关联,可以使用通配符(*)字符关联相同的事务属性的设置方法;
     * 传播事务(默认的){@link org.springframework.transaction.annotation.Propagation#REQUIRED}
     */
    private              String[] customizeRequiredMethodRuleTransactionAttributes     = {};
    
    
    /** 
    * @Description: 事务管理器
    * @Param: [transactionManager] 
    * @return: org.springframework.transaction.interceptor.TransactionInterceptor 
    * @Author: Leslie
    * @Date: 2018/7/16 
    */ 
    @Bean(CUSTOMIZE_TRANSACTION_INTERCEPTOR_NAME)
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager transactionManager){
        NameMatchTransactionAttributeSource  attributeSource=new NameMatchTransactionAttributeSource();
        RuleBasedTransactionAttribute readOnly=this.readOnlyTransactionRule();
        RuleBasedTransactionAttribute require=this.requireTrabsactionRule();
        //默认只读事务配置
        for (String methodName:DEFAULT_READ_ONLY_METHOD_RULE_TRANSACTION_ATTRIBUTES){
            attributeSource.addTransactionalMethod(methodName,readOnly);
        }
        //默认传播事务配置
        for (String methodName:DEFAULT_REQUIRED_METHOD_RULE_TRANSACTION_ATTRIBUTES){
            attributeSource.addTransactionalMethod(methodName,require);
        }
        //定制只读事务配置
        for (String methodName:customizeReadOnlyMethodRuleTransactionAttributes){
            attributeSource.addTransactionalMethod(methodName,require);
        }
        //定制传播事务配置
        for (String methodName:customizeRequiredMethodRuleTransactionAttributes){
            attributeSource.addTransactionalMethod(methodName,require);
        }
        return new TransactionInterceptor(transactionManager,attributeSource);

    }
    /** 
    * @Description: 配置事务拦截
    * @Param: [] 
    * @return: org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator 
    * @Author: Leslie
    * @Date: 2018/7/16 
    */
    @Bean
    public BeanNameAutoProxyCreator customizeTransactionBeanNameAutoProxyCreator (){
        BeanNameAutoProxyCreator autoProxyCreator = new BeanNameAutoProxyCreator();
        autoProxyCreator.setInterceptorNames(CUSTOMIZE_TRANSACTION_INTERCEPTOR_NAME );
        //默认
        for (String defaultTransactionBeanNameSuffix:DEFAULT_TRANSACTION_BEAN_NAMES){
            autoProxyCreator.setBeanNames(defaultTransactionBeanNameSuffix);
        }
        //定制
        for (String customizeTransactionBeanName:customizeTransactionBeanNames){
            autoProxyCreator.setBeanNames(customizeTransactionBeanName);
        }
        autoProxyCreator.setProxyTargetClass(true);
        return autoProxyCreator;
    }
    /**
    * @Description: 只读事务配置
    * @Param: []
    * @return: org.springframework.transaction.interceptor.RuleBasedTransactionAttribute
    * @Author: Leslie
    * @Date: 2018/7/16
    */
    private RuleBasedTransactionAttribute readOnlyTransactionRule() {
        RuleBasedTransactionAttribute readOnly=new RuleBasedTransactionAttribute();
        readOnly.setReadOnly(true);
        readOnly.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        return readOnly;
    }
    /** 
    * @Description: 支持当前事务，不存在则建立一个新的事务
    * @Param: [] 
    * @return: org.springframework.transaction.interceptor.RuleBasedTransactionAttribute 
    * @Author: Leslie
    * @Date: 2018/7/16 
    */ 
    private RuleBasedTransactionAttribute requireTrabsactionRule(){
        RuleBasedTransactionAttribute require = new RuleBasedTransactionAttribute();
        require.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        require.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        require.setTimeout(TransactionDefinition.TIMEOUT_DEFAULT);
        return require;
    }


}
