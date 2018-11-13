package com.springboot.web.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @program: education-parent
 * @description: redis
 * @author: Leslie
 * @create: 2018-09-07 15:48
 **/
@Configuration
@EnableCaching
public class RedisConfig {
    private Logger logger=LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.pool.max-active}")
    private int redisPoolMaxActive;
    @Value("${spring.redis.pool.max-idle}")
    private int redisPoolMaxIdle;
    @Value("${spring.redis.pool.min-idle}")
    private int redisPoolMinIdle;
    @Value("${spring.redis.pool.max-wait}")
    private int redisPoolMaxWait;
    @Value("${spring.redis.database}")
    private int dbIndex;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value(value = "${spring.redis.database}")
    private Integer serviceDataBase;


    /**
     * 采用jackson的json序列化
     * @param factory
     * @return
     */

    @Bean
    public RedisTemplate<String,String> redisTemplate(JedisConnectionFactory factory){
        StringRedisTemplate stringRedisTemplate=new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer=new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        //设置value序列化
        stringRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        //hash序列化
        stringRedisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        stringRedisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        stringRedisTemplate.setKeySerializer(jackson2JsonRedisSerializer);
        stringRedisTemplate.afterPropertiesSet();
        return stringRedisTemplate;
    }

    @Bean
    public JedisConnectionFactory factory(JedisPoolConfig poolCofig){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(host);
        factory.setPort(port);
        factory.setPassword(password);
        //testOnBorrow为true时，返回的连接是经过测试可用的
        factory.setPoolConfig(poolCofig);
        factory.setDatabase(serviceDataBase);
        logger.info("建立新的redis连接");
        return factory;
    }

    @Bean
    public JedisPoolConfig poolCofig() {
        JedisPoolConfig poolCofig = new JedisPoolConfig();
        poolCofig.setMaxIdle(redisPoolMaxIdle);
        poolCofig.setMinIdle(redisPoolMinIdle);
        poolCofig.setMaxTotal(redisPoolMaxActive);
        poolCofig.setMaxWaitMillis(redisPoolMaxWait);
        poolCofig.setTestOnBorrow(true);
        return poolCofig;
    }

}
