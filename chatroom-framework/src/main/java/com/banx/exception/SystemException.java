package com.banx.exception;

import com.banx.enums.AppHttpCodeEnum;
import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {

    private final int code;
    private final String msg;

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMessage());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMessage();
    }

}
