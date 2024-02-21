package com.banx.service.impl;

import com.banx.constant.RedisKeyConstants;
import com.banx.core.LoginUser;
import com.banx.core.entity.User;
import com.banx.domain.pojo.UserInfo;
import com.banx.domain.vo.LoginVo;
import com.banx.mapper.UserMapper;
import com.banx.service.UserService;
import com.banx.utils.BeanCopyUtils;
import com.banx.utils.JwtUtils;
import com.banx.utils.RedisCache;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-02-02 17:38:09
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private AuthenticationManager authenticationManager;
    private RedisCache redisCache;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public LoginVo login(String username, String password) {
        // 验证
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        // 未通过,验证失败会在UserDetailsServiceImpl抛出异常
        if(Objects.isNull(authenticate)){
            return null;
        }
        // 通过则创建Jwt
        LoginUser loginUser = (LoginUser)authenticate.getPrincipal();
        User user = loginUser.getUser();
        String id = user.getId().toString();
        String jwt = JwtUtils.creatJwt(id);
        // 转化userInfo
        UserInfo userInfo = BeanCopyUtils.copyBean(user, UserInfo.class);
        // LoginUser存入redis
        redisCache.setCacheObject(RedisKeyConstants.LOGIN_PREFIX+ id,loginUser);
        // 返回Jwt
        return new LoginVo(jwt,userInfo);
    }

    @Override
    public Set<UserInfo> onlineUsers() {
        // 从redis中获取userId集合
        Set<String> ids = redisCache.getCacheSet(RedisKeyConstants.ONLINE_USERS);
        // 如果ids为空集合，直接返回
        if(ids.isEmpty()) return new HashSet<>();
        // 从数据库获取user信息
        List<User> users = userMapper.selectBatchIds(ids);
        // 转化成 userInfo
        List<UserInfo> userInfos = BeanCopyUtils.copyBeanList(users, UserInfo.class);
        // 返回
        return new HashSet<>(userInfos);
    }
}

