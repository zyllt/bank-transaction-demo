package com.github.miles.transaction.service;

import java.util.UUID;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * 交易ID生成器
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@Component
@RequiredArgsConstructor
public class TransactionIdGenerator {

    /**
     * 生成交易ID
     * 
     * @return 交易ID
     */
    public String nextTransactionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
