package com.hsbc.demo.transaction.api.base;

import lombok.Getter;
import lombok.Setter;

/**
 * @author miles.zeng
 * @since 2025-03-12
 */
@Getter
@Setter
public class BaseResponse<T> {

    private int code;

    private String msg;

    private T data;

    public BaseResponse() {
        this(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage());
    }

    public BaseResponse(int code, String msg) {
        this(code, msg, null);
    }

    public BaseResponse(T data) {
        this(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), data);
    }

    public BaseResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
