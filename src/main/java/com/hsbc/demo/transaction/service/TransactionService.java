package com.hsbc.demo.transaction.service;

import com.hsbc.demo.transaction.api.base.PageResult;
import com.hsbc.demo.transaction.dto.TransactionDTO;
import com.hsbc.demo.transaction.dto.request.CreateTransactionReq;
import com.hsbc.demo.transaction.dto.request.UpdateTransactionReq;

import jakarta.annotation.Nonnull;

/**
 * Transaction Service Interface
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
public interface TransactionService {

    /**
     * Create transaction
     * 
     * @param memberId User ID
     * @param request  Transaction request data
     * @return Created transaction
     */
    TransactionDTO createTransaction(@Nonnull Long memberId, @Nonnull CreateTransactionReq request);

    /**
     * Update transaction
     * 
     * @param transactionId Transaction ID
     * @param request       Transaction request data
     * @return Updated transaction
     */
    TransactionDTO updateTransaction(@Nonnull String transactionId, @Nonnull UpdateTransactionReq request);

    /**
     * Delete transaction
     * 
     * @param transactionId Transaction ID
     */
    void deleteTransaction(@Nonnull String transactionId, @Nonnull Long memberId);

    /**
     * Get transaction
     * 
     * @param transactionId Transaction ID
     * @return Transaction
     */
    TransactionDTO getTransaction(@Nonnull String transactionId, @Nonnull Long memberId);

    /**
     * Get all transactions with pagination
     * 
     * @param page Page number
     * @param size Page size
     * @return Transaction list
     */
    PageResult<TransactionDTO> pageListTransactions(int page, int size);
}