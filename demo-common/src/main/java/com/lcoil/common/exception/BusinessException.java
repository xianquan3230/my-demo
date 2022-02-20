package com.lcoil.common.exception;

/**
 * @Classname BusinessException
 * @Description TODO
 * @Date 2022/2/19 8:36 PM
 * @Created by l-coil
 */
public class BusinessException extends RuntimeException{
    private int errorCode;
    public BusinessException(int errorCode){
        this.errorCode = errorCode;
    }
    public BusinessException(String message){
        super(message);
    }
    public BusinessException(String message,int errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message,Throwable cause,int errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message,Throwable cause,boolean enableSuppression,boolean writableStackTrace,int errorCode){
        super(message,cause,enableSuppression,writableStackTrace);
        this.errorCode = errorCode;
    }
}
