package com.springboot.web.config.druid;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @program: My Study
 * @description: 动态数据源切换
 * @author: Leslie
 * @create: 2018-07-13 14:19
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDbType();
    }
}
