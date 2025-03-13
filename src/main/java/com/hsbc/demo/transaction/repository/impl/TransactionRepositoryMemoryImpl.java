package com.hsbc.demo.transaction.repository.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.hsbc.demo.transaction.model.TransactionPO;
import com.hsbc.demo.transaction.repository.TransactionRepository;

/**
 * In-memory Mock Implementation
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@Repository
public class TransactionRepositoryMemoryImpl implements TransactionRepository {

    /**
     * Primary key generator
     */
    private final AtomicLong idGenerator = new AtomicLong(0);

    /**
     * Transaction storage, transactionId -> transactionPO
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
     * Get transactions with pagination, default sorted by transaction time in ascending order
     * 
     * @param page Page number
     * @param size Page size
     * @return Transaction list
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
