package com.github.miles.transaction;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

import com.github.miles.transaction.constants.TransactionType;
import com.github.miles.transaction.dto.TransactionDTO;
import com.github.miles.transaction.dto.request.CreateTransactionReq;


/**
 * 基础测试类,用于数据构建
 * 
 * @author miles.zeng
 * @since  
 */
@SuppressWarnings("unused")
public class BaseTest {

    protected CreateTransactionReq validRequest;

    protected TransactionDTO mockDTO;

    protected String testTransactionId;

    protected String accountId;
    
    protected String opponentAccountId;

    @BeforeEach
    void setUp() {
        testTransactionId = UUID.randomUUID().toString().replace("-", "");
        accountId = "12345678";
        opponentAccountId = "12345678_";
        BigDecimal amount = new BigDecimal("100.00");
        validRequest = new CreateTransactionReq(
                accountId,
                opponentAccountId,
                amount,
                TransactionType.DEPOSIT.getType(),
                "Test transaction");

        mockDTO = new TransactionDTO();
        mockDTO.setId(1L);
        mockDTO.setTransactionId(testTransactionId);
        mockDTO.setMemberId(1L);
        mockDTO.setTransactionTime(System.currentTimeMillis());
        mockDTO.setUpdatedTime(System.currentTimeMillis());
        mockDTO.setAccountId(accountId);
        mockDTO.setOpponentAccountId(opponentAccountId);
        mockDTO.setAmount(amount);
        mockDTO.setTransactionType(TransactionType.DEPOSIT.getType());
        mockDTO.setDescription("Test transaction");
    }

}
