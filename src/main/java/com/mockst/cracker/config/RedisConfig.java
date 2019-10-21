package com.mockst.cracker.config;

import com.mockst.cracker.util.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Auther: zhiwei
 * @Date: 2019/8/7 23:10
 * @Description:
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,String> redisTemplate(LettuceConnectionFactory factory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    @Bean
    public RedisUtil redisUtil(RedisTemplate<String,String> redisTemplate){
        return new RedisUtil(redisTemplate);
    }

}
