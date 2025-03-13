package com.github.miles.transaction.exception;

import java.util.stream.Collectors;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.miles.transaction.api.base.ApiCode;
import com.github.miles.transaction.api.base.BaseResponse;
import com.github.miles.transaction.api.base.helper.ResponseHelper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 全局异常处理
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 交易异常处理
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(TransactionException.class)
    public Mono<BaseResponse<?>> handleTransactionException(TransactionException ex) {
        log.error("transaction exception", ex);
        return Mono.just(ResponseHelper.failure(ex.getCode(), ex.getMessage()));
    }

    /**
     * 参数验证异常处理
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<BaseResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return Mono.defer(() -> {
            log.error("illegal request parameter", ex);
            String errorMsg = ex.getBindingResult().getAllErrors().stream()
                    .map(error -> {
                        String fieldName = ((FieldError) error).getField();
                        String errorMessage = error.getDefaultMessage();
                        return fieldName + ":" + errorMessage;
                    }).collect(Collectors.joining(","));
            BaseResponse<?> errorResponse = ResponseHelper.failure(ApiCode.ERROR_PARAMETER.getCode(), errorMsg);
            return Mono.just(errorResponse);
        });
    }

    /**
     * 全局异常处理
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Mono<BaseResponse<?>> handleGlobalException(Exception ex) {
        log.error("global exception", ex);
        return Mono.just(ResponseHelper.failure(ApiCode.SERVER_ERROR.getCode(), ex.getMessage()));
    }
}