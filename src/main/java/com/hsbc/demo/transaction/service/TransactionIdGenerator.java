package com.hsbc.demo.transaction.service;

import java.util.UUID;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Transaction ID Generator
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@Component
@RequiredArgsConstructor
public class TransactionIdGenerator {

    /**
     * Generate transaction ID
     * 
     * @return Transaction ID
     */
    public String nextTransactionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
