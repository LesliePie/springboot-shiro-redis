package com.springboot.web.config.druid;

import com.springboot.web.annotation.DataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @program: My Study
 * @description: 切换数据源
 * @author: Leslie
 * @create: 2018-07-13 11:59
 **/
@Aspect
@Order(-10)
@Component
public class DataSourceAspect {
    protected Logger logger=LoggerFactory.getLogger(DataSourceAspect.class);

    /**
     * @Before("@annotation(DataSource)")的意思是： @Before：在方法执行之前进行执行： @annotation(DataSource)：
     * 会拦截注解DataSource的方法，否则不拦截;
     *
     */
    @Before(value = "@annotation(dataSource)")
    public void afterReturning(JoinPoint point, DataSource dataSource){
        //获取当前指定数据源
        String dsId=dataSource.value();
        //如果不在我们注入数据范围之内，给出警告，系统使用默认数据源
      if (!DataSourceContextHolder.containsDataSource(dsId)){
          logger.warn("{}数据源不存在,将使用默认的数据源{}",dsId,DataSourceContextHolder.PRIMARY_DATA_SOURCE);
      }else {
          DataSourceContextHolder.setDbType(dsId);
      }
    }
   /**
   * @Description: 方法使用后的判定
   * @Param: [point, dataSource]
   * @return: void
   * @Author: Leslie
   * @Date: 2018/7/13
   */
    public void restoreDataSource(JoinPoint point,DataSource dataSource){
        logger.info("Revert DataSource > "+dataSource.value()+">"+point.getSignature());
        //方法使用后进行销毁
        DataSourceContextHolder.clearDbType();
    }

}
