package com.hsbc.demo.transaction.exception;

import java.util.stream.Collectors;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hsbc.demo.transaction.api.base.ApiCode;
import com.hsbc.demo.transaction.api.base.BaseResponse;
import com.hsbc.demo.transaction.api.base.helper.ResponseHelper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Global Exception Handler
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Transaction exception handler
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
     * Parameter validation exception handler
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
     * Global exception handler
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