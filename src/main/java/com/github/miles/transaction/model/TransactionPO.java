package com.github.miles.transaction.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 交易实体类
 * @author miles.zeng
 * @since  2025-03-12
 */
@Getter
@Setter
public class TransactionPO {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 交易ID
     */
    private String transactionId;
    /**
     * 用户ID
     */
    private Long memberId;
    /**
     * 账户号
     */
    private String accountId;
    /**
     * 对手账户号
     */
    private String opponentAccountId;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 交易类型
     */
    private Integer transactionType;
    /**
     * 描述
     */
    private String description;
    /**
     * 交易时间
     */
    private Long transactionTime;
    /**
     * 更新时间
     */
    private Long updatedTime;
    

} 