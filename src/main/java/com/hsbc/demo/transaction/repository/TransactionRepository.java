package com.hsbc.demo.transaction.repository;

import java.util.List;

import com.hsbc.demo.transaction.model.TransactionPO;

/**
 * Transaction Repository   
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
public interface TransactionRepository {

    /**
     * Save transaction
     * 
     * @param transactionPO
     * @return
     */
    TransactionPO save(TransactionPO transactionPO);

    /**
     * Find transaction by transaction ID
     * 
     * @param transactionId
     * @return
     */ 
    TransactionPO findByTransactionId(String transactionId);

    /**
     * Update transaction
     * 
     * @param transaction
     * @return
     */ 
    int update(TransactionPO transaction);

    /**
     * Delete transaction
     * 
     * @param transactionId
     * @param memberId
     * @return
     */ 
    int delete(String transactionId, Long memberId);

    /**
     * Count total transactions
     * 
     * @return
     */ 
    int count();

    /**
     * Get transactions with pagination
     * 
     * @param page
     * @param size
     * @return
     */ 
    List<TransactionPO> pageList(int page, int size);
}