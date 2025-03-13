package com.hsbc.demo.transaction.api.base.helper;

import com.hsbc.demo.transaction.api.base.ApiCode;
import com.hsbc.demo.transaction.api.base.BaseResponse;

import lombok.experimental.UtilityClass;

/**
 * Response Helper Class
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@UtilityClass
public class ResponseHelper {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(data);
    }

    public static <T> BaseResponse<T> failure() {
        return failure(ApiCode.SERVER_ERROR);
    }

    public static <T> BaseResponse<T> failure(ApiCode apiCode) {
        return failure(apiCode.getCode(), apiCode.getMessage());
    }

    public static <T> BaseResponse<T> failure(String message) {
        return failure(ApiCode.SERVER_ERROR.getCode(), message);
    }

    public static <T> BaseResponse<T> failure(Integer code, String message) {
        return new BaseResponse<T>(code, message);
    }

    public static boolean isFailure(BaseResponse<?> response) {
        return !isSuccess(response);
    }

    public static boolean isSuccess(BaseResponse<?> response) {
        return response != null && ApiCode.SUCCESS.getCode() == response.getCode();
    }

}
