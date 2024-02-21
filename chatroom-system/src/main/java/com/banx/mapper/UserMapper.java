package com.banx.mapper;

import com.banx.core.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2024-02-02 17:38:08
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

