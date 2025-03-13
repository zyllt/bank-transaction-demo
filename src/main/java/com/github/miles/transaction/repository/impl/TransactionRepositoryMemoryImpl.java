package com.github.miles.transaction.repository.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.github.miles.transaction.model.TransactionPO;
import com.github.miles.transaction.repository.TransactionRepository;

/**
 * 内存Mock实现
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@Repository
public class TransactionRepositoryMemoryImpl implements TransactionRepository {

    /**
     * 主键生成器
     */
    private final AtomicLong idGenerator = new AtomicLong(0);

    /**
     * 交易存储,transactionId -> transactionPO
     */
    private final Map<String, TransactionPO> transactionMap = new ConcurrentHashMap<>();


    @Override
    public TransactionPO save(TransactionPO transactionPO) {
        transactionPO.setId(idGenerator.incrementAndGet());
        transactionMap.put(transactionPO.getTransactionId(), transactionPO);
        return transactionPO;
    }

    @Override
    public TransactionPO findByTransactionId(String transactionId) {
        return transactionMap.get(transactionId);
    }

    @Override
    public int update(TransactionPO transaction) {
        return transactionMap.replace(transaction.getTransactionId(), transaction) == null ? 0 : 1;
    }

    @Override
    public int delete(String transactionId, Long memberId) {
        return transactionMap.remove(transactionId) == null ? 0 : 1;
    }

    @Override
    public int count() {
        return transactionMap.size();
    }

    /**
     * 分页查询交易,默认按照交易时间正序
     * 
     * @param page 页码
     * @param size 每页大小
     * @return 交易列表
     */
    @Override
    public List<TransactionPO> pageList(int page, int size) {
        return transactionMap
                .values()
                .stream()
                .sorted(Comparator.comparing(TransactionPO::getTransactionTime))
                .skip((long) (page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }


}
