package com.netease.homework.utils.exception;

import com.netease.homework.utils.result.ResultCode;

public class GlobalException extends RuntimeException {
    /**
     * 异常码
     */
    private Integer code;

    public GlobalException(GlobalException e){
        super(e.getMessage());
        this.code =  e.getCode();
    }
    public GlobalException(ResultCode code){
        super(code.getMessage());
        this.code = code.getCode();
    }
    public GlobalException(Integer code, String message){
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}