package com.banx.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long id;

    private String userName;//用户名

    private String nickName;//昵称

    private String description;//个性签名

    private String avatar;//头像

}
