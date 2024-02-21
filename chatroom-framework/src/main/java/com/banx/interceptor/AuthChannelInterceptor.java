package com.banx.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.banx.constant.RedisKeyConstants;
import com.banx.core.LoginUser;
import com.banx.enums.AppHttpCodeEnum;
import com.banx.exception.SystemException;
import com.banx.service.WebSocketPrincipal;
import com.banx.utils.JwtUtils;
import com.banx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;


/**
 * 在拦截器中保存当前用户
 * 指定用户发送message需要
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 降低优先级，使在security之后
public class AuthChannelInterceptor implements ChannelInterceptor {

    private final RedisCache redisCache;

    @Autowired
    public AuthChannelInterceptor(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        // 获取 STOMP 消息头处理器
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        // 检查 STOMP 命令是否为 CONNECT（连接建立）
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 解析token
            String token = accessor.getFirstNativeHeader("token");
            LoginUser loginUser = this.verifyToken(token);
            // 创建Principal对象
            Principal principal = new WebSocketPrincipal(loginUser);
            // 将当前身份验证信息设置到 STOMP 消息头中的用户属性
            accessor.setUser(principal);
        }
        return message;
    }

    /**
     * 判断token是否存在、超时。不合法会抛出异常
     * @return 根据Subject从redis中获取LoginUser对象
     */
    private LoginUser verifyToken(String token){
        // 验证token
        DecodedJWT verify;
        try {
            verify = JwtUtils.parseJwt(token);
        } catch (Exception e) {
            // token超时,token非法
            //全局异常处理在controller层，filter层在此之前,这里的会被security中定义的异常处理器处理
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 解析token,拿到token中的userId
        String userId = verify.getSubject();
        // 从redis中获取对象并返回
        return redisCache.getCacheObject(RedisKeyConstants.LOGIN_PREFIX + userId);
    }
}
