package com.github.miles.transaction.repository.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Nonnull;
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
     * 交易存储,transactionId:transactionTime -> transactionPO
     * <p>
     * 使用transactionTime排序
     */
    private final ConcurrentSkipListMap<TransactionIdAndTimeKey, TransactionPO> transactionMap = new ConcurrentSkipListMap<>(
            Comparator.comparing(TransactionIdAndTimeKey::transactionTime));

    @Override
    public TransactionPO save(TransactionPO transactionPO) {
        transactionPO.setId(idGenerator.incrementAndGet());
        transactionMap.put(
                new TransactionIdAndTimeKey(transactionPO.getTransactionId(), transactionPO.getTransactionTime()),
                transactionPO);
        return transactionPO;
    }

    @Override
    public TransactionPO findByTransactionId(String transactionId) {
        return transactionMap.get(new TransactionIdAndTimeKey(transactionId, null));
    }

    @Override
    public int update(TransactionPO transaction) {
        return transactionMap.replace(
                new TransactionIdAndTimeKey(transaction.getTransactionId(), transaction.getTransactionTime()),
                transaction) == null ? 0 : 1;
    }

    @Override
    public int delete(String transactionId, Long memberId) {
        return transactionMap.remove(new TransactionIdAndTimeKey(transactionId, null)) == null ? 0 : 1;
    }

    @Override
    public int count() {
        return transactionMap.size();
    }

    /**
     * 分页查询交易,以transactionTime排序
     * 
     * @param page 页码
     * @param size 每页大小
     * @return 交易列表
     */
    @Override
    public List<TransactionPO> pageList(int page, int size) {
        return transactionMap.sequencedEntrySet()
                .stream()
                .skip((long) (page - 1) * size)
                .limit(size)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    /**
     * 交易ID组合时间的key,以transactionId为主键,实现了equals和hashcode
     * <p>
     * 方便模拟排序分页
     * <p>
     * 类似联合索引
     */
    public record TransactionIdAndTimeKey(@Nonnull String transactionId, @Nonnull Long transactionTime) {
        @Override
        public int hashCode() {
            return Objects.hash(transactionId);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            TransactionIdAndTimeKey that = (TransactionIdAndTimeKey) obj;
            return transactionId.equals(that.transactionId);
        }
    }
}
