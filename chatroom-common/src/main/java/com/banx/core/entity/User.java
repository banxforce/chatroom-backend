package com.banx.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户表(User)表实体类
 *
 * @author makejava
 * @since 2024-02-02 17:38:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("cr_user")
public class User {

    private Long id;//主键

    private String userName;//用户名

    private String nickName;//昵称

    private String password;//密码

    private String description;//个性签名

    private String type;//用户类型：0代表普通用户，1代表管理员

    private String status;//账号状态（0正常 1停用）

    private String email;//邮箱

    private String phonenumber;//手机号

    private String sex;//用户性别（0男，1女，2未知）

    private String avatar;//头像

    private Long createBy;//创建人的用户id

    private Date createTime;//创建时间

    private Long updateBy;//更新人

    private Date updateTime;//更新时间

    private Integer delFlag;//删除标志（0代表未删除，1代表已删除）
    

}

