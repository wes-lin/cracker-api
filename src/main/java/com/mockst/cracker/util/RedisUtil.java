package com.mockst.cracker.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: zhiwei
 * @Date: 2019/8/7 23:16
 * @Description:
 */
public class RedisUtil {

    private RedisTemplate redisTemplate;

    public RedisUtil(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key,Object value){
        ValueOperations<String,Object> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }

    public void set(String key,Object value,long s){
        ValueOperations<String,Object> vo = redisTemplate.opsForValue();
        vo.set(key, value,s, TimeUnit.SECONDS);
    }

    public Object get(String key) {
        ValueOperations<String,Object> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }

    public boolean del(String key){
        return redisTemplate.delete(key);
    }

}
