package com.springboot.web.config.shiro;

import com.springboot.web.config.redis.RedisCacheManager;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: parent
 * @description: shiro配置
 * @author: Leslie
 * @create: 2018-11-08 10:22
 **/
@Configuration
public class ShiroConfig {
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * shiro生命周期管理
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
        return lifecycleBeanPostProcessor;
    }

    /**
     * shiro的缓存管理器，此处采用我们自定义实现的
     * @return
     */
    @Bean
    public CacheManager shiroRedisCacheManager() {
        return new RedisCacheManager();
    }
    @Bean
    public static DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        /**
         * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
         * 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，导致返回404。
         * 加入这项配置能解决这个bug
         */
        autoProxyCreator.setUsePrefix(true);
        return autoProxyCreator;
    }

    /**
     * 安全管理器
     * @return
     */
    @Bean(value = "securityManager")
    public DefaultWebSecurityManager getManager(){
        DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
        manager.setRealm(new UserRealm());
        RedisCacheManager redisCacheManager=new RedisCacheManager();
        redisCacheManager.setRedisTemplate(redisTemplate);
        manager.setCacheManager(redisCacheManager);
        /**
         *
         *
         * 关闭shiroSession
         */
        DefaultSubjectDAO subjectDAO=new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator=new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
    }

    @Bean(value = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean factoryBean=new ShiroFilterFactoryBean();
        //添加自己实现的一个过滤器
        Map<String,Filter> filterMap=new HashMap<>();
        filterMap.put("jwt",new JwtFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        /**
         * 自定义url规则
         */
        factoryBean.setUnauthorizedUrl("/api/401");
        Map<String,String> filterRuleMap=new HashMap<>();
        // 访问401和404页面不通过我们的Filter
        filterRuleMap.put("/api/401", "anon");
        filterRuleMap.put("/druid/*","anon");
        filterRuleMap.put("/api/authorization/login","anon");
        //放行swagger
        filterRuleMap.put("/swagger-ui.html", "anon");
        filterRuleMap.put("/swagger-resources", "anon");
        filterRuleMap.put("/v2/api-docs","anon");
        filterRuleMap.put("/doc.html","anon");
        filterRuleMap.put("/webjars/springfox-swagger-ui/**", "anon");
        // 所有请求通过我们自己的JWT Filter
        filterRuleMap.put("/**", "jwt");
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    /**
     *shiro  Spring AOP权限注解
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }


}
