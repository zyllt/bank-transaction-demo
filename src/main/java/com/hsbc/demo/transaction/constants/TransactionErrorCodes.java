package com.hsbc.demo.transaction.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
/**
 * Error Codes
 * 
 * @author miles.zeng
 * @since  
 */
@Getter
@RequiredArgsConstructor
public enum TransactionErrorCodes {

    TRANSACTION_NOT_FOUND(1001, "交易不存在"),
    TRANSACTION_PARAM_INVALID(1002, "交易参数非法"),
    TRANSACTION_NO_PERMISSION(1003, "无权限操作"),
    ;

    private final int code;
    private final String message;

}
