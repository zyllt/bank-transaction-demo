package com.hsbc.demo.transaction.dto;

import com.hsbc.demo.transaction.dto.request.CreateTransactionReq;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Transaction DTO
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