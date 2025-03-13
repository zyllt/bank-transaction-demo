
package com.github.miles.transaction.api.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** 
 * 基础响应码
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@RequiredArgsConstructor
@Getter
public enum ApiCode {

    SUCCESS(0, "success"),
    ERROR_PARAMETER(400, "error parameter"),
    ERROR_AUTHORIZE(401, "error authorize"),
    ERROR_FORBIDDEN(403, "error forbidden"),
    ERROR_NOTFOUND(404, "error not found"),
    SERVER_ERROR(500, "server error"),
    SERVER_LIMIT(503, "server limit");

    private final int code;
    private final String message;

}
