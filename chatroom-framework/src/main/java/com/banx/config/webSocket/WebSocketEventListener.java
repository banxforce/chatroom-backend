package com.banx.config.webSocket;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.banx.enums.AppHttpCodeEnum;
import com.banx.exception.SystemException;
import com.banx.pojo.Shout;
import com.banx.service.WebSocketPrincipal;
import com.banx.utils.JwtUtils;
import com.banx.utils.WebSocketUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
public class WebSocketEventListener implements ApplicationListener<ApplicationEvent> {
    private  WebSocketUtils webSocketUtils;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketEventListener(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Autowired
    public void setWebSocketUtils(WebSocketUtils webSocketUtils) {
        this.webSocketUtils = webSocketUtils;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent event) {
        if (event instanceof SessionConnectEvent) {
            handleSessionConnectEvent((SessionConnectEvent) event);
        } else if (event instanceof SessionDisconnectEvent) {
            handleSessionDisconnectEvent((SessionDisconnectEvent) event);
        }
    }

    // 监听连接
    private void handleSessionConnectEvent(SessionConnectEvent event) {
        // 从事件中获取请求实例
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        // 验证token
        this.verifyToken(accessor);
        // 获取 Principal
        WebSocketPrincipal principal = (WebSocketPrincipal) event.getUser();

        // 得到id
        String id = principal.getUserId();
        // set集合保存所有在线用户
        webSocketUtils.link(id);
        // 在这里使用 login 和 passcode 进行自定义逻辑
        System.out.println("用户连接，curUser: " + principal.getLoginUser());
        // 广播通知所有客户端
        this.broadcast("上", principal);
    }

    // 监听断开连接，长连接，断开不需要验证
    private void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
        // 获取 Principal
        WebSocketPrincipal principal = (WebSocketPrincipal) event.getUser();
        // 得到id
        String id = principal.getUserId();
        // 从Set集合中去除离开的用户
        webSocketUtils.unlink(id);
        // 执行连接断开时的自定义逻辑
        System.out.println("WebSocket 连接关闭: " + principal.getLoginUser());
        // 广播通知所有客户端
        this.broadcast("下", principal);
    }

    /**
     * 解析并验证请求实例的header中的token，验证通过存入this.curUser
     * @param accessor 从event中获取的请求实例
     */
    private void verifyToken(StompHeaderAccessor accessor){
        // 获取header中的token
        String token = accessor.getFirstNativeHeader("token");
        // 验证token
        DecodedJWT verify;
        try {
            verify = JwtUtils.parseJwt(token);
            String userId = verify.getSubject();
        } catch (Exception e) {
            // token超时,token非法
            //全局异常处理在controller层，filter层在此之前,所以需要处理掉
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
    }

    /**
     * 广播进行通知
     */
    private void broadcast(String type, Principal principal){
        Shout shout = new Shout();
        shout.getChatMsg().setMsg(principal.getName() + type + "线了！");
        this.simpMessagingTemplate.convertAndSend("/topic/link",shout);
    }

}
