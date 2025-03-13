package com.hsbc.demo.transaction.constants;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Transaction Types
 *  
 * @author miles.zeng
 * @since 2025-03-12
 */
@RequiredArgsConstructor
@Getter
public enum TransactionType {   
    /**
     * Deposit
     */
    DEPOSIT(1, "存款"), 
    /**
     * Withdrawal
     */
    WITHDRAWAL(2, "取款"),  
    /**
     * Transfer
     */
    TRANSFER(3, "转账"), 
    /**
     * Payment
     */
    PAYMENT(4, "支付"),
    /**
     * Refund
     */
    REFUND(5, "退款"),
    /**
     * Other
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