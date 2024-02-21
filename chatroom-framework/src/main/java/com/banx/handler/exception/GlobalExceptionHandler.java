package com.banx.handler.exception;

import com.banx.core.ResponseResult;
import com.banx.enums.AppHttpCodeEnum;
import com.banx.exception.SystemException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public ResponseResult<Object> systemException(SystemException e){
        // 打印日志
        log.error("出现了异常!", e);
        // 从异常对象获取信息并封装返回
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult<Object> exception(Exception e){
        // 打印日志
        log.error("出现了异常!", e);
        // 从异常对象获取信息并封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,e.getMessage());
    }

}
