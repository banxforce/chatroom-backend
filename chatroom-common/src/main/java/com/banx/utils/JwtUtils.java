package com.banx.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;


import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class JwtUtils {

    @Value("${token.secret}")
    private static final String JWT_KEY = "MyFirstBlog";//Jwt密钥
    @Value("${token.expireTime}")
    private static final Integer JWT_TTL = 60 * 60 * 24; //一天
    private static final Algorithm JWT_HEADER = Algorithm.HMAC256(JWT_KEY); // 使用 HMAC256 算法和提供的密钥定义 JWT 头部的算法
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

    /**
     * 生成jwt
     * @param subject token中要存放的数据
     * @return 根据subject和默认失效时间设置的jwt令牌
     */
    public static String creatJwt(String subject){
        return jwtBuild(subject,null);
    }

    /**
     * 生成jwt
     * @param subject token中要存放的数据
     * @param ttlSeconds 过期时间,秒
     * @return 根据subject和默认失效时间设置的jwt令牌
     */
    public static String creatJwt(String subject,Integer ttlSeconds){
        return jwtBuild(subject, ttlSeconds);
    }

    private static String jwtBuild(String subject,Integer ttlSeconds){
        //获取日历实例
        Calendar instance = Calendar.getInstance();
        //获取目前时间
        Date now = instance.getTime();
        //计算过期时间，如果参数为null,就设定为默认值
        if(Objects.isNull(ttlSeconds)){
            ttlSeconds = JWT_TTL;
        }
        // 设置过期时间
        instance.add(Calendar.SECOND,ttlSeconds);
        //创建jwt令牌
        return JWT.create()
                .withJWTId(getUUID()) //唯一的ID
                .withSubject(subject) //主体内容
                .withExpiresAt(instance.getTime()) //过期时间
                .withIssuedAt(now) //签发时间
                .withIssuer("bx") //签发者
                .sign(JWT_HEADER); //使用HS256对称加密算法签名

    }

    /**
     * 解析 JWT 字符串并验证其有效性
     *
     * @param jwt 要解析和验证的 JWT 字符串
     * @return 解析后的 DecodedJWT 对象，包含了 JWT 中的声明信息
     */
    public static DecodedJWT parseJwt(String jwt)throws JWTVerificationException {
        // 通过加密后的 jwtHeader 获取解析器
        JWTVerifier jwtVerifier = JWT.require(JWT_HEADER).build();
        // 解析 JWT 字符串并验证签名的有效性,验证失败会抛出 JWTVerificationException
        DecodedJWT verify = jwtVerifier.verify(jwt);
        // 返回解析后的 DecodedJWT 对象
        return verify;
    }

}