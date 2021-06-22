package com.qbk.webfilter.resp;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResultUtil {

    public <T> Result<T> ok(String msg,T data){
        return Result.create(ResultStatus.SUCCESS.getErrorCode(),msg,data);
    }

    public <T> Result<T> ok(T data){
       return ok(ResultStatus.SUCCESS.getErrorMsg(),data);
    }

    public <T> Result<T> ok(){
        return ok(null);
    }

    public <T> Result<T> error(){
        return Result.create(ResultStatus.FAIL.getErrorCode(),ResultStatus.FAIL.getErrorMsg(),null);
    }

    public <T> Result<T> error(String msg){
        return Result.create(ResultStatus.FAIL.getErrorCode(),msg,null);
    }

    public  <T> Result<T> common(ResultStatus resultStatus ,T data){
        return Result.create(resultStatus.getErrorCode(),resultStatus.getErrorMsg(),data);
    }
    public  <T> Result<T> common(ResultStatus resultStatus ){
        return Result.create(resultStatus.getErrorCode(),resultStatus.getErrorMsg(),null);
    }

    public Result<Object> common(Integer code, String msg) {
        return Result.create(code,msg,null);
    }

    public <T> Result<T> common(Integer code, String msg  ,T data) {
        return Result.create(code,msg,data);
    }
}
