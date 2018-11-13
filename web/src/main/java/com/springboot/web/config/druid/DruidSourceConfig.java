package com.springboot.web.config.druid;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: web
 * @description: 数据源配置
 * @author: Leslie
 * @create: 2018-07-13 16:54
 **/
@Configuration
public class DruidSourceConfig {
    private Logger logger=LoggerFactory.getLogger(DruidSourceConfig.class);
    /**
     * 连接url
     */
    @Value("${spring.datasource.url}")
    private String dbUrl;
    /**
     * 用户名
     */
    @Value("${spring.datasource.username}")
    private String username;
    /**
     * 用户密码
     */
    @Value("${spring.datasource.password}")
    private String password;
    /**
     * 数据库加载的类
     */
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${custom.datasource.url}")
    private String customUrl;
    @Value("${custom.datasource.username}")
    private String customUserName;
    @Value("${custom.datasource.password}")
    private String customPassWord;
    @Value("${custom.datasource.driver-class-name}")
    private String customDriverClassName;


    @Value("${pool.jdbc.initialSize}")
    private int initialSize;
    @Value("${pool.jdbc.minIdle}")
    private int minIdle;
    @Value("${pool.jdbc.maxActive}")
    private int maxActive;
    @Value("${pool.jdbc.maxWait}")
    private int maxWait;
    @Value("${pool.jdbc.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${pool.jdbc.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    @Value("${pool.jdbc.validationQuery}")
    private String validationQuery;
    @Value("${pool.jdbc.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${pool.jdbc.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${pool.jdbc.testOnReturn}")
    private boolean testOnReturn;
    @Value("${pool.jdbc.poolPreparedStatements}")
    private boolean poolPreparedStatements;
    @Value("${pool.jdbc.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${pool.jdbc.filters}")
    private String filters;
    @Value("${pool.jdbc.connectionProperties}")
    private String connectionProperties;
    @Value("${pool.jdbc.useGlobalDataSourceStat}")
    private boolean useGlobalDataSourceStat;
    /** 
    * @Description: 注册一个StatViewServlet
    * @Param: [] 
    * @return: org.springframework.boot.web.servlet.ServletRegistrationBean 
    * @Author: Leslie
    * @Date: 2018/7/13 
    */
    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        //org.springframework.boot.context.embedded.ServletRegistrationBean提供类的进行注册.
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //添加初始化参数：initParams

        //白名单：
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
//        servletRegistrationBean.addInitParameter("deny", "192.168.0.151");
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", "root");
        servletRegistrationBean.addInitParameter("loginPassword", "admin");
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }
    /** 
    * @Description: Druid监控过滤器
    * @Param: [] 
    * @return: org.springframework.boot.web.servlet.FilterRegistrationBean 
    * @Author: Leslie
    * @Date: 2018/7/13 
    */
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new WebStatFilter());
        registrationBean.setName("druidWebStatFilter");
        Map<String,String> map=new HashMap<>();
        map.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        registrationBean.setInitParameters(map);
        registrationBean.addUrlPatterns("/*");
        DelegatingFilterProxy proxy=new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        return registrationBean;
    }

    /**
     * 数据源1
     * @return
     */
    @Bean(name = "dataSource")
    @Primary
    public DataSource primaryDataSource() {
        DruidDataSource dataSource=new DruidDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setPoolPreparedStatements(poolPreparedStatements);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            List<Filter> proxyFilters = new ArrayList<Filter>();
            WallFilter statFilter = new WallFilter();
            WallConfig config = new WallConfig();
            // 批量操作
            config.setMultiStatementAllow(true);
            statFilter.setConfig(config);
            proxyFilters.add(statFilter);
            dataSource.setProxyFilters(proxyFilters);
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        dataSource.setConnectionProperties(connectionProperties);
        return dataSource;
    }
    /**
     * @Description: 数据源2
     * @Param: []
     * @return: javax.sql.DataSource
     * @Author: Leslie
     * @Date: 2018/7/13
     */
    public DataSource otherDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(customUrl);
        druidDataSource.setUsername(customUserName);
        druidDataSource.setPassword(customPassWord);
        druidDataSource.setDriverClassName(customDriverClassName);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setMaxWait(maxWait);
        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setValidationQuery(validationQuery);
        druidDataSource.setTestWhileIdle(testWhileIdle);
        druidDataSource.setTestOnBorrow(testOnBorrow);
        druidDataSource.setTestOnReturn(testOnReturn);
        druidDataSource.setPoolPreparedStatements(poolPreparedStatements);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            List<Filter> proxyFilters = new ArrayList<Filter>();
            WallFilter statFilter = new WallFilter();
            WallConfig config = new WallConfig();
            // 批量操作
            config.setMultiStatementAllow(true);
            statFilter.setConfig(config);
            proxyFilters.add(statFilter);
            druidDataSource.setProxyFilters(proxyFilters);
            druidDataSource.setFilters(filters);
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        druidDataSource.setConnectionProperties(connectionProperties);
        return druidDataSource;
    }

    public DynamicDataSource dynamicDataSource()throws SQLException{
        //默认数据源
        DataSource dataSource=primaryDataSource();
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object,Object> targetDataSource=new HashMap<>();
        //设置默认数据源
        // 要添加的话在写一个获取数据源的方法
        targetDataSource.put(DataSourceContextHolder.PRIMARY_DATA_SOURCE,dataSource);
        dynamicDataSource.setTargetDataSources(targetDataSource);
        return dynamicDataSource;
    }

}
