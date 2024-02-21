package com.banx;

import com.banx.utils.RedisCache;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
public class MainTest {
    @Resource
    private  BCryptPasswordEncoder passwordEncoder;
    @Resource
    private RedisCache redisCache;
    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void redis(){
        System.out.println(redisTemplate.keys("*"));
    }

}
