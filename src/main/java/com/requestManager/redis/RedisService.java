package com.requestManager.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Service
public class RedisService {

    @Autowired
    RedisTemplate<String, Object> redisCommands;

    public String get(String key) {
        return (String) redisCommands.opsForValue().get(key);
    }

    public void set(String key, String value, Duration time) {
        redisCommands.opsForValue().set(key, value);
        redisCommands.expire(key, time.toMillis(), TimeUnit.MILLISECONDS);
    }

    public void del(String key) {
        redisCommands.opsForValue().getAndDelete(key);
    }



    public void expire(String key, Duration time) {
        redisCommands.expire(key, time.toMillis(), TimeUnit.MILLISECONDS);
    }
}
