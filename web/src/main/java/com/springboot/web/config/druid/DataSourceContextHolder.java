package com.springboot.web.config.druid;

/**
 * @program: My Study
 * @description: 数据源切换
 * @author: Leslie
 * @create: 2018-07-13 14:02
 **/
public class DataSourceContextHolder {

    public static final String PRIMARY_DATA_SOURCE="PRIMARY_DATA_SOURCE";
    public static final String DATA_SOURCE_B="DATA_SOURCE_B ";

    /**
     *  数据源类型
     */
    private static ThreadLocal<String> contextHolder=new ThreadLocal<>();
    /** 
    * @Description: 设置数据源类型
    * @Param: [dbType] 
    * @return: void 
    * @Author: Leslie
    * @Date: 2018/7/13 
    */ 
    public static void setDbType(String dbType){
        contextHolder.set(dbType);        
    }
    /**
    * @Description: 获取数据源类型
    * @Param: []
    * @return: java.lang.String
    * @Author: Leslie
    * @Date: 2018/7/13
    */
    public static String getDbType(){
        return contextHolder.get();
    }
    /**
    * @Description: 清楚数据源类型
    * @Param: []
    * @return: void
    * @Author: Leslie
    * @Date: 2018/7/13
    */
    public static void clearDbType(){
        contextHolder.remove();
    }
    /**
    * @Description: 判断当前数据源类型是否存在
    * @Param: [dsId]
    * @return: boolean
    * @Author: Leslie
    * @Date: 2018/7/13
    */

    public static boolean containsDataSource(String dsId){
        if (null==dsId){
            return false;
        }else{
            return PRIMARY_DATA_SOURCE.equals(dsId) || DATA_SOURCE_B.equals(dsId);
        }
    }
}
