package com.springboot.service;

import com.alibaba.fastjson.JSONObject;
import com.springboot.common.base.PageRequest;
import com.springboot.common.base.PageResult;
import com.springboot.common.utill.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @program: education-parent
 * @description: redis 工具类
 * @author: Leslie
 * @create: 2018-07-19 10:17
 **/
@Component
@SuppressWarnings("unchecked")
public class RedisUtil {
    private Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private RedisTemplate redisTemplate;



    /**
     * @Description: 清空所有缓存
     * @Param: []
     * @return: void
     * @Author: Leslie
     * @Date: 2018/7/19
     */
    public void clear() {
        redisTemplate.execute((RedisCallback) redisConnection -> {
            redisConnection.flushDb();
            return "OK";
        });
    }

    /**
     * @Description: 批量移除key
     * @Param: [keys]
     * @return: void
     * @Author: Leslie
     * @Date: 2018/7/19
     */
    public void remove(final String... keys) {
        for (String item : keys) {
            remove(item);
        }
    }

    /**
     * @Description: 移除该对应value
     * @Param: [key]
     * @return: void
     * @Author: Leslie
     * @Date: 2018/7/19
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * @Description: 判断是否存在该key
     * @Param: [key]
     * @return: boolean
     * @Author: Leslie
     * @Date: 2018/7/19
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * @Description: 读取缓存
     * @Param: [key]
     * @return: java.lang.Object
     * @Author: Leslie
     * @Date: 2018/7/19
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * value值加一
     * @param key
     * @param count
     */
    public void increseKey(String key,Long count){
        try {
            redisTemplate.opsForValue().increment(key,count);
        }catch (Exception e){
            logger.error("缓存写入失败，key为{}", key);
            e.printStackTrace();
        }
    }
    /**
     * @Description: 写入缓存
     * @Param: [key, value]
     * @return: boolean
     * @Author: Leslie
     * @Date: 2018/7/19
     */
    public boolean set(String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            logger.error("缓存写入失败，key为{}", key);
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    /**
     * HashGet
     *
     * @param key
     * @param item
     * @return
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hash所有键值
     *
     * @param key
     * @return
     */
    public Map<String, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }
    /**
     * 获取hash对应对象值
     */
    public <T> T hmget(String key,Class<T> t){
        Map<String,Object> map=hmget(key);
        return BeanUtils.mapToBean(map,t);
    }
    /**
     * @Description: 写入缓存
     * @Param: [key, value, expireTime]
     * @return: boolean
     * @Author: Leslie
     * @Date: 2018/7/19
     */
    public boolean set(String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            logger.error("缓存写入失败，key为{}", key);
        } finally {
            return result;
        }
    }

    /**
     * hashSet
     *
     * @param key
     * @param map
     * @return
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * hashSet并设置时间
     *
     * @param key
     * @param map
     * @param time
     * @return
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向hashSet放入并设置键值，不存在则新建
     *
     * @param key
     * @param item
     * @param value
     * @return
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * hashSet放入键值，并设置过期时间
     *
     * @param key
     * @param item
     * @param value
     * @param time
     * @return
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  not null
     * @param item 可以多个，不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该值
     *
     * @param key
     * @param item
     * @return
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增，不存在则创建一个，并返回现在的值
     *
     * @param key
     * @param item
     * @param by 大于0
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key
     * @param item
     * @param by
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 根据key获取set中的值
     * @param key
     * @return
     */
    public Set<Object> sGet(String key){
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value 从一个set中进行查询，是否存在
     * @param key
     * @param value
     * @return
     */
    public boolean sHasKey(String key,Object value){
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * set放入缓存
     * @param key
     * @param values
     * @return
     */
    public long sSet(String key, Object...values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * set中是否含有该值
     * @param key
     * @param value
     * @return
     */
    public boolean sIsMember(String key,Object value){
        try {
            return redisTemplate.opsForSet().isMember(key,value);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * set放入缓存
     * @param key
     * @param time
     * @param values
     * @return  成功个数
     */
    public long sSetAndTime(String key,long time,Object...values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if(time>0) {
               redisTemplate.expire(key,time,TimeUnit.SECONDS) ;
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set长度
     * @param key
     * @return
     */
    public long sGetSetSize(String key){
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key
     * @param values 可以多个
     * @return
     */
    public long setRemove(String key, Object ...values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //=======list====================

    /**
     * 获取list里的内容
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<Object> lGet(String key,long start, long end){
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Object> lGet(PageRequest page) {
        Map<String,Object> map=page.getCondition();
        int nowPage=page.getNowPage();
        int pageSize=page.getPageSize();
        int start=(nowPage-1)*pageSize;
        int end=start+pageSize;
        if (!map.isEmpty()&&map.get("parentId")!=null){
            String key=map.get("parentId").toString();
            return  lGet(key,start,end);
        }else{
            return lGet("parentCode",start,end);
        }
    }

    /**
     * 获取list的长度
     * @param key
     * @return
     */
    public long lGetListSize(String key){
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取list长度
     * @param page
     * @return
     */
    public long lGetListSize(PageRequest page) {
        Map<String,Object> map=page.getCondition();
        if (!map.isEmpty()&&map.get("parentId")!=null){
            String key=map.get("parentId").toString();
            return  lGetListSize(key);
        }else{
            return lGetListSize("parentCode");
        }
    }
    /**
     * 通过索引获取List的值
     * @param key
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key,long index){
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 存入list
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPushAll(key,JSONObject.toJSON(value));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
  

    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            if (time > 0) {
             redisTemplate.expire(key,time,TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * list放入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * list 放入缓存
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            if (time > 0) {
             redisTemplate.expire(key,time,TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list的某个值
     * @param key
     * @param index
     * @param value
     * @return
     */
    public boolean lUpdateIndex(String key, long index,Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除 n个值为value的
     * @param key
     * @param count
     * @param value
     * @return
     */
    public long lRemove(String key,long count,Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 从尾部进行移除
     * @param key
     * @return
     */
    public <T> T rPop(String key,Class<T> t){
        try {
            JSONObject object= (JSONObject) redisTemplate.opsForList().rightPop(key);
            return object.toJavaObject(t);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取zSet Size
     * @param key
     * @return
     */
    public long zSetGetSize(String key){
        try {
            Long size = redisTemplate.opsForZSet().size(key);
            return size;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * zSet 添加
     * @param key
     * @param value
     * @param score
     */
    public void zSetSetValue(String key,Object value,Double score){
        try {
             redisTemplate.opsForZSet().add(key,value,score);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("redis插入失败");
        }
    }

    /**
     * redis zSet移除
     * @param key
     * @param values
     */
    public void zSetRemove(String key,Object... values){
        try {
            redisTemplate.opsForZSet().remove(key,values);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("redis移除失败");
        }
    }

    public boolean zSetIsMember(String key,Object value){
        try {
            Long range= redisTemplate.opsForZSet().rank(key,value);
            if (range!=null&&range>=0){
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * zSet获取
     * @param key
     * @param value1
     * @param value2
     * @return
     */
    public Set<Object> zSetRangeByScore(String key,Double value1,Double value2){
        try {
            Set<Object> set=  redisTemplate.opsForZSet().rangeByScore(key,value1,value2);
            return  set;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * ZzSet 根据index获取
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> zSetRevRange(String key,Long start,Long end){
        try {
            Set<Object> set=  redisTemplate.opsForZSet().reverseRange(key,start,end);
            return  set;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PageResult<Object> zSetPage(String key, Integer nowPage, Integer pageSize){

        try {
            Long size = redisTemplate.opsForZSet().size(key);
            if (size!=null||size==0){
                return null;
            }
            Long total=size/pageSize;
            Integer totalPages=(total.intValue())+(size%pageSize==0?0:1);
            Integer start=(nowPage-1)*pageSize;
            Integer end=nowPage*pageSize-1;
            //使用排序
            Set<Object> set= redisTemplate.opsForZSet().range(key,start,end);
            return  new PageResult<>(size.intValue(),totalPages,nowPage,pageSize,new ArrayList<>(set));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 增加score
     * @param key
     * @param value
     * @param score
     */
    public void zSetIncrementScore(String key,Object value,Double score) {
        try {
            redisTemplate.opsForZSet().incrementScore(key,value,score);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 分页获取score 和value
     * @param key
     * @param start
     * @param end
     * @return
     */
    public  Set<ZSetOperations.TypedTuple<Object>> zSetRangeByScoreWithScores(String key,Long start,Long end){
        try {
            Set<ZSetOperations.TypedTuple<Object>> set= redisTemplate.opsForZSet().rangeWithScores(key,start,end);
            return set;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    /**
     * 根据key 和item 获取score
     * @param key
     * @param value
     * @return
     */
    public Double zSetScore(String key,Object value){
        try {
            Double score= redisTemplate.opsForZSet().score(key,value);
            return score;
        }catch (Exception e){
            e.printStackTrace();
            return 0D;
        }
    }
}