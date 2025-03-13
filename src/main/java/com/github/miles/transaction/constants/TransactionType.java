package com.github.miles.transaction.constants;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 交易类型
 *  
 * @author miles.zeng
 * @since 2025-03-12
 */
@RequiredArgsConstructor
@Getter
public enum TransactionType {   
    /**
     * 存款
     */
    DEPOSIT(1, "存款"), 
    /**
     * 取款
     */
    WITHDRAWAL(2, "取款"),  
    /**
     * 转账
     */
    TRANSFER(3, "转账"), 
    /**
     * 支付
     */
    PAYMENT(4, "支付"),
    /**
     * 退款
     */
    REFUND(5, "退款"),
    /**
     * 其他
     */
    OTHER(6, "其他");

    private final int type;

    private final String description;

    public static Optional<TransactionType> from(int type) {
        return Arrays.stream(TransactionType.values())
            .filter(t -> t.getType() == type)
            .findFirst();
    }
}