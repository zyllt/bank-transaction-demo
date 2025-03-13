package com.github.miles.transaction.repository;

import java.util.List;

import com.github.miles.transaction.model.TransactionPO;

/**
 * 交易Repository   
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
public interface TransactionRepository {

    /**
     * 保存交易
     * 
     * @param transactionPO
     * @return
     */
    TransactionPO save(TransactionPO transactionPO);

    /**
     * 根据交易ID查询交易
     * 
     * @param transactionId
     * @return
     */ 
    TransactionPO findByTransactionId(String transactionId);

    /**
     * 更新交易
     * 
     * @param transaction
     * @return
     */ 
    int update(TransactionPO transaction);

    /**
     * 删除交易
     * 
     * @param transactionId
     * @param memberId
     * @return
     */ 
    int delete(String transactionId, Long memberId);

    /**
     * 查询交易总数
     * 
     * @return
     */ 
    int count();

    /**
     * 分页查询交易
     * 
     * @param page
     * @param size
     * @return
     */ 
    List<TransactionPO> pageList(int page, int size);
}