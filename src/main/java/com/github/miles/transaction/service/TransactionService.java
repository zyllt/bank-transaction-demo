package com.github.miles.transaction.service;

import com.github.miles.transaction.api.base.PageResult;
import com.github.miles.transaction.dto.TransactionDTO;
import com.github.miles.transaction.dto.request.CreateTransactionReq;
import com.github.miles.transaction.dto.request.UpdateTransactionReq;

import jakarta.annotation.Nonnull;

/**
 * 交易服务接口
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
public interface TransactionService {

    /**
     * 创建交易
     * 
     * @param memberId 用户ID
     * @param request  交易请求数据
     * @return 创建的交易
     */
    TransactionDTO createTransaction(@Nonnull Long memberId, @Nonnull CreateTransactionReq request);

    /**
     * 更新交易
     * 
     * @param transactionId 交易ID
     * @param request       交易请求数据
     * @return 更新的交易
     */
    TransactionDTO updateTransaction(@Nonnull String transactionId, @Nonnull UpdateTransactionReq request);

    /**
     * 删除交易
     * 
     * @param transactionId 交易ID
     */
    void deleteTransaction(@Nonnull String transactionId, @Nonnull Long memberId);

    /**
     * 获取交易
     * 
     * @param transactionId 交易ID
     * @return 交易
     */
    TransactionDTO getTransaction(@Nonnull String transactionId, @Nonnull Long memberId);

    /**
     * 分页获取所有交易
     * 
     * @param page 页码
     * @param size 每页大小
     * @return 交易列表
     */
    PageResult<TransactionDTO> pageListTransactions(int page, int size);
}