package com.banx.pojo;

import com.banx.constant.WebSocketConstants;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Shout {

    public Shout() {
        this.fromUsername = WebSocketConstants.FROM_USERNAME_SYSTEM;
        this.time = new Date();
        this.chatMsg = new ChatMsg();
    }

    private String toUsername;
    private String fromUsername; // “system”代表系统通知
    private ChatMsg chatMsg; // 聊天消息对象
    private Date time; // 发送时间，yyyy-MM-dd HH:mm:ss

}
