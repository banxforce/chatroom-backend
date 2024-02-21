package com.banx.controller;

import com.banx.pojo.Shout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    /**
     * 广播消息，不指定用户，所有订阅此的用户都能收到消息
     */
    @MessageMapping("/broadcastShout")
    public void broadcast(Shout shout) {
        this.simpMessagingTemplate.convertAndSend("/topic/shouts", shout);
    }

    /**
     * 一对一聊天
     * @param principal ChannelInterceptor中放入的
     * @param shout STOMP消息的负载
     */
    @MessageMapping("/singleShout")
    public void singleUser(Principal principal,@Payload Shout shout){
        String toUsername = shout.getToUsername();
        this.simpMessagingTemplate.convertAndSend("/queue/shouts/" + toUsername, shout);
    }

    /**
     * 统一异常处理
     */
    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public Exception handleExceptions(Exception e){
        e.printStackTrace();
        return e;
    }

}
