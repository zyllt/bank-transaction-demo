package com.hsbc.demo.transaction.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Transaction Entity Class
 * @author miles.zeng
 * @since  2025-03-12
 */
@Getter
@Setter
public class TransactionPO {

    /**
     * Primary key ID
     */
    private Long id;
    /**
     * Transaction ID
     */
    private String transactionId;
    /**
     * User ID
     */
    private Long memberId;
    /**
     * Account number
     */
    private String accountId;
    /**
     * Opponent account number
     */
    private String opponentAccountId;
    /**
     * Amount
     */
    private BigDecimal amount;
    /**
     * Transaction type
     */
    private Integer transactionType;
    /**
     * Description
     */
    private String description;
    /**
     * Transaction time
     */
    private Long transactionTime;
    /**
     * Update time
     */
    private Long updatedTime;
    

} 