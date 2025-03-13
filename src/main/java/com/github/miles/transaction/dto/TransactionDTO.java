package com.github.miles.transaction.dto;

import com.github.miles.transaction.dto.request.CreateTransactionReq;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 交易DTO
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransactionDTO extends CreateTransactionReq {
    
    private Long id;
    private String transactionId;
    private Long memberId;
    private Long transactionTime;
    private Long updatedTime;
} 