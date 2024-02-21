package com.banx.domain.vo;

import com.banx.domain.pojo.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {
    private String token;
    private UserInfo userInfo;
}
