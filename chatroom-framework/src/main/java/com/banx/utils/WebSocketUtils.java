package com.banx.utils;

import com.banx.constant.RedisKeyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketUtils {
    private final  RedisCache redisCache;

    @Autowired
    public WebSocketUtils(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    public  void link(String uid){
        redisCache.addCacheSet(RedisKeyConstants.ONLINE_USERS, uid);
    }

    public  void unlink(String uid) {
        redisCache.removeCacheSet(RedisKeyConstants.ONLINE_USERS, uid);
    }

}
