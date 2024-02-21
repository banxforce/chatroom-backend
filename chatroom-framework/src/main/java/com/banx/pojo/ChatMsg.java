package com.banx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMsg {
    private String avatar;
    private String nickName;
    private String time; // "1:18 PM"
    private String msg;
    private int chatType; // 信息类型，0文字，1图片, 2文件
    private String id; // userId
    private Map<String,Object> extend; // imgType: 1, //(1表情，2本地图片)
}
