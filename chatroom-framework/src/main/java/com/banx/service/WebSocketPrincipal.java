package com.banx.service;

import com.banx.core.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketPrincipal implements Principal {

    private LoginUser loginUser;

    @Override
    public String getName() {
        return this.loginUser.getUsername();
    }

    public String getUserId(){
        return this.loginUser.getUserId().toString();
    }
}
