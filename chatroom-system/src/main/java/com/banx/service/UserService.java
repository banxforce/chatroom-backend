package com.banx.service;

import com.banx.core.entity.User;
import com.banx.domain.pojo.UserInfo;
import com.banx.domain.vo.LoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-02-02 17:38:09
 */
public interface UserService extends IService<User> {
    LoginVo login(String username, String password);

    Set<UserInfo> onlineUsers();
}

