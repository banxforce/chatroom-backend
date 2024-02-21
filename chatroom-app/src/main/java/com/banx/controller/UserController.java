package com.banx.controller;

import com.banx.core.LoginUser;
import com.banx.core.ResponseResult;
import com.banx.core.entity.User;
import com.banx.domain.pojo.UserInfo;
import com.banx.dto.LoginBody;
import com.banx.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseResult<Object> login(@RequestBody LoginBody loginBody){
        return new ResponseResult<>().ok(userService.login(loginBody.getUsername(), loginBody.getPassword()));
    }

    @GetMapping("/onlineUsers")
    public  ResponseResult<Object> onlineUsers(@RequestParam Long id){
        Set<UserInfo> result = this.userService.onlineUsers();
        return new ResponseResult<>().ok(
                result.stream()
                        // 排除本身
                        .filter( userInfo -> !Objects.equals(userInfo.getId(), id))
                        .collect(Collectors.toSet())
        );
    }
}
