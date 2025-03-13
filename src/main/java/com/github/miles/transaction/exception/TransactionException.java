package com.github.miles.transaction.exception;

import com.github.miles.transaction.constants.TransactionErrorCodes;

import com.github.miles.transaction.api.base.ApiCode;

import lombok.Getter;

/**
 * 交易异常
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@Getter
public class TransactionException extends RuntimeException {

    private final int code;

    public TransactionException(int code, String message) {
        super(message);
        this.code = code;
    }

    public TransactionException(Throwable cause) {
        this(ApiCode.SERVER_ERROR.getCode(), cause.getMessage(), cause);
    }

    public TransactionException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }


    /**
     * 无权限操作
     *  
     * @author miles.zeng
     * @since 2025-03-12
     */
    public static class TransactionNoPermissionException extends TransactionException {

        public TransactionNoPermissionException() {
            this(TransactionErrorCodes.TRANSACTION_NO_PERMISSION.getMessage());
        }

        public TransactionNoPermissionException(String message) {
            super(TransactionErrorCodes.TRANSACTION_NO_PERMISSION.getCode(), message);
        }
    }

    /**
     * 交易参数非法异常
     * 
     * @author miles.zeng
     * @since 2025-03-12
     */
    public static class TransactionParamInvalidException extends TransactionException {
        public TransactionParamInvalidException() {
            this(TransactionErrorCodes.TRANSACTION_PARAM_INVALID.getMessage());
        }

        public TransactionParamInvalidException(String message) {
            super(TransactionErrorCodes.TRANSACTION_PARAM_INVALID.getCode(), message);
        }
    }

    /**
     * 交易不存在异常
     * 
     * @author miles.zeng
     * @since 2025-03-12
     */
    public static class TransactionNotFoundException extends TransactionException {

        public TransactionNotFoundException() {
            this(TransactionErrorCodes.TRANSACTION_NOT_FOUND.getMessage());
        }

        public TransactionNotFoundException(String message) {
            super(TransactionErrorCodes.TRANSACTION_NOT_FOUND.getCode(), message);
        }
    }

}
