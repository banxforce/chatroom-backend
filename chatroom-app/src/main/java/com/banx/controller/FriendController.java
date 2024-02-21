package com.banx.controller;

import com.banx.core.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @GetMapping("/friendList")
    public ResponseResult<?> friendList(){
        return null;
    }

}
