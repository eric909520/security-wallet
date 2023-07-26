package org.secwallet.core.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Result<T> {
    @ApiModelProperty(value ="Return flags: success flag = 0, failure flag = 1")
    private int code;

    @ApiModelProperty(value = "Return message")
    private String message;


    @ApiModelProperty(value = "data")
    private T data;

    public Result() {
    }

    ;

    public static Result fail(String msg) {
        Result result = new Result();
        result.setMessage(msg);
        result.setCode(ResultCode.FAIL.getCode());
        return result;
    }

    public static Result fail(Object data, String msg) {
        Result result = new Result();
        result.setMessage(msg);
        result.setCode(ResultCode.FAIL.getCode());
        result.setData(data);
        return result;
    }

    public static Result fail(String msg, int code) {
        Result result = new Result();
        result.setMessage(msg);
        result.setCode(code);
        return result;
    }

    public static Result<String> fail(ResultCode resultCode) {
        return fail(resultCode.getMsg(), resultCode.getCode());
    }

    public static Result fail(Object data, String msg, int code) {
        Result result = new Result();
        result.setMessage(msg);
        result.setCode(code);
        result.setData(data);
        return result;
    }

    public static Result fail() {
        Result result = new Result();
        result.setMessage(ResultCode.FAIL.getMsg());
        result.setCode(ResultCode.FAIL.getCode());
        return result;
    }

    public static Result ok(Object data, String msg) {
        Result result = new Result();
        result.setMessage(msg);
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setData(data);
        return result;
    }

    public static Result ok(Object data) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    public static Result ok() {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMsg());
        return result;
    }
}
