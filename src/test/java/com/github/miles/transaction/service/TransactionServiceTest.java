package com.github.miles.transaction.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.miles.transaction.BaseTest;
import com.github.miles.transaction.api.base.PageResult;
import com.github.miles.transaction.constants.DemoConstants;
import com.github.miles.transaction.constants.TransactionType;
import com.github.miles.transaction.dto.TransactionDTO;
import com.github.miles.transaction.dto.request.CreateTransactionReq;
import com.github.miles.transaction.dto.request.UpdateTransactionReq;
import com.github.miles.transaction.exception.TransactionException.TransactionNoPermissionException;
import com.github.miles.transaction.exception.TransactionException.TransactionNotFoundException;
import com.github.miles.transaction.exception.TransactionException.TransactionParamInvalidException;
/**
 * 
 * Test for the TransactionService
 * 
 * @author miles
 * @version 1.0 
 * @since 2025-03-13
 */
@SpringBootTest
class TransactionServiceTest extends BaseTest{

    @Autowired
    private TransactionService transactionService;


    @Test
    void testWhenCreateTransactionReturnSuccess() {
        TransactionDTO response = transactionService.createTransaction(DemoConstants.DEMO_MEMBER_ID,validRequest);
        // Then the transaction should be created successfully
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(validRequest.getAccountId(), response.getAccountId());
        assertEquals(validRequest.getAmount(), response.getAmount());
        assertEquals(validRequest.getTransactionType(), response.getTransactionType());
        assertEquals(validRequest.getDescription(), response.getDescription());

        //验证完清除
        transactionService.deleteTransaction(response.getTransactionId(), DemoConstants.DEMO_MEMBER_ID);
    }

    @Test
    void testWhenCreateTransactionRequestIsNullShouldThrowException() {
        assertThrows(NullPointerException.class, () -> {
            transactionService.createTransaction(DemoConstants.DEMO_MEMBER_ID, null);
        });
    }
    
    @Test
    void testWhenCreateTransactionParamInvalidShouldThrowException() {
        //参数不正确
        CreateTransactionReq invalidRequest = new CreateTransactionReq();
        invalidRequest.setAccountId(null);
        invalidRequest.setOpponentAccountId(null);
        invalidRequest.setAmount(null);
        invalidRequest.setTransactionType(null);
        invalidRequest.setDescription(null);

        assertThrows(TransactionParamInvalidException.class, () -> {
            transactionService.createTransaction(DemoConstants.DEMO_MEMBER_ID, invalidRequest);
        });
    }

    @Test
    void testWhenCreateTransactionAccountIdNotPassedShouldThrowException() {
        CreateTransactionReq invalidRequest = new CreateTransactionReq();
        invalidRequest.setAccountId(null);
        invalidRequest.setOpponentAccountId("12345678_");
        invalidRequest.setAmount(new BigDecimal("200.00"));
        invalidRequest.setTransactionType(TransactionType.WITHDRAWAL.getType());
        invalidRequest.setDescription("test");

        assertThrows(TransactionParamInvalidException.class, () -> {
            transactionService.createTransaction(DemoConstants.DEMO_MEMBER_ID, invalidRequest);
        });
    }

    @Test
    void testWhenCreateTransactionOpponentAccountIdNotPassedShouldThrowException() {
        CreateTransactionReq invalidRequest = new CreateTransactionReq();
        invalidRequest.setAccountId("12345678_");
        invalidRequest.setOpponentAccountId(null);
        invalidRequest.setAmount(new BigDecimal("200.00"));
        invalidRequest.setTransactionType(TransactionType.WITHDRAWAL.getType());
        invalidRequest.setDescription("test");

        assertThrows(TransactionParamInvalidException.class, () -> {
            transactionService.createTransaction(DemoConstants.DEMO_MEMBER_ID, invalidRequest);
        });
    }

    @Test
    void testWhenGetTransactionReturnSuccess() {
        //先创建一个
        TransactionDTO createdTransaction = transactionService.createTransaction(DemoConstants.DEMO_MEMBER_ID,validRequest);

        TransactionDTO response = transactionService.getTransaction(createdTransaction.getTransactionId(),DemoConstants.DEMO_MEMBER_ID);
        
        assertNotNull(response);
        assertEquals(createdTransaction.getTransactionId(), response.getTransactionId());

        //验证完清除
        transactionService.deleteTransaction(createdTransaction.getTransactionId(), DemoConstants.DEMO_MEMBER_ID);
    }
    
    @Test
    void testWhenGetTransactionNotFoundShouldThrowException() {
        //不存在transactionId
        String nonExistentId = UUID.randomUUID().toString().replace("-", "");
        
        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.getTransaction(nonExistentId,DemoConstants.DEMO_MEMBER_ID);
        });
    }
    
    @Test
    void testWhenUpdateTransactionReturnSuccess() {
        //先创建一个
        TransactionDTO createdTransaction = transactionService.createTransaction(DemoConstants.DEMO_MEMBER_ID,validRequest);

        // Given an updated transaction request
        UpdateTransactionReq updateRequest = new UpdateTransactionReq();
        updateRequest.setAccountId("87654321");
        updateRequest.setOpponentAccountId("12345678_");
        updateRequest.setAmount(new BigDecimal("200.00"));
        updateRequest.setTransactionType(TransactionType.WITHDRAWAL.getType());
        updateRequest.setDescription("Updated transaction");
        
        TransactionDTO response = transactionService.updateTransaction(createdTransaction.getTransactionId(), updateRequest);
        
        assertNotNull(response);
        assertEquals(createdTransaction.getTransactionId(), response.getTransactionId());
        assertEquals(updateRequest.getAccountId(), response.getAccountId());
        assertEquals(updateRequest.getOpponentAccountId(), response.getOpponentAccountId());
        assertEquals(updateRequest.getAmount(), response.getAmount());
        assertEquals(updateRequest.getTransactionType(), response.getTransactionType());
        assertEquals(updateRequest.getDescription(), response.getDescription());

        //验证完清除
        transactionService.deleteTransaction(createdTransaction.getTransactionId(), DemoConstants.DEMO_MEMBER_ID);
    }

    //更新参数不合法
    @Test
    void testWhenUpdateTransactionParamInvalidShouldThrowException() {
        //先创建一个
        TransactionDTO createdTransaction = transactionService.createTransaction(DemoConstants.DEMO_MEMBER_ID,validRequest);
        
        UpdateTransactionReq updateRequest = new UpdateTransactionReq();
        updateRequest.setAccountId(null);
        updateRequest.setOpponentAccountId(null);
        updateRequest.setAmount(null);
        updateRequest.setTransactionType(null);
        updateRequest.setDescription(null);

        assertThrows(TransactionParamInvalidException.class, () -> {
            transactionService.updateTransaction(createdTransaction.getTransactionId(), updateRequest);
        });

        //验证完清除
        transactionService.deleteTransaction(createdTransaction.getTransactionId(), DemoConstants.DEMO_MEMBER_ID);  
    }
    
    @Test
    void testWhenUpdateTransactionNotFoundShouldThrowException() {
        //不存在transactionId
        String nonExistentId = UUID.randomUUID().toString().replace("-", "");
        
        UpdateTransactionReq updateRequest = new UpdateTransactionReq();
        updateRequest.setAccountId("87654321");
        updateRequest.setOpponentAccountId("12345678_");
        updateRequest.setAmount(new BigDecimal("200.00"));
        updateRequest.setTransactionType(TransactionType.WITHDRAWAL.getType());
        updateRequest.setDescription("Updated transaction");

        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.updateTransaction(nonExistentId, updateRequest);
        });
    }

    //更新时transactionId没有传
    @Test
    void testWhenUpdateTransactionTransactionIdNotPassedShouldThrowException() {
        UpdateTransactionReq updateRequest = new UpdateTransactionReq();
        updateRequest.setAccountId("87654321");
        updateRequest.setOpponentAccountId("12345678_");
        updateRequest.setAmount(new BigDecimal("200.00"));

        assertThrows(TransactionParamInvalidException.class, () -> {
            transactionService.updateTransaction(null, updateRequest);
        });
    }
    
    @Test
    void testWhenSameMemberIdDeleteTransactionSuccess() {
        //创建一个交易
        TransactionDTO createdTransaction = transactionService.createTransaction(DemoConstants.DEMO_MEMBER_ID,validRequest);
        String transactionIdToDelete = createdTransaction.getTransactionId();
        
        transactionService.deleteTransaction(transactionIdToDelete, DemoConstants.DEMO_MEMBER_ID);
        
        // Then the transaction should be deleted
        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.getTransaction(transactionIdToDelete,DemoConstants.DEMO_MEMBER_ID);
        });
    }
    
    @Test
    void testWhenDifferentMemberIdDeleteTransactionShouldThrowException() {
         //创建一个交易
         TransactionDTO createdTransaction = transactionService.createTransaction(DemoConstants.DEMO_MEMBER_ID,validRequest);
         String transactionIdToDelete = createdTransaction.getTransactionId();
        
        assertThrows(TransactionNoPermissionException.class, () -> {
            transactionService.deleteTransaction(transactionIdToDelete, DemoConstants.DEMO_MEMBER_ID+1);
        });

        //验证完清除
        transactionService.deleteTransaction(transactionIdToDelete, DemoConstants.DEMO_MEMBER_ID);
    }

    //删除一个不存在的,正常返回
    @Test
    void testWhenDeleteNonExistentTransactionShouldSuccess() {
        String nonExistentId = UUID.randomUUID().toString().replace("-", "");
        transactionService.deleteTransaction(nonExistentId, DemoConstants.DEMO_MEMBER_ID);
    }

    
    @Test
    void testWhenPageListTransactionsAllReturnSuccess() throws Exception {
        //创建5个
        Map<String,TransactionDTO> createdTransactions = new HashMap<>();
        int count = 5;
        for (int i = 0; i < count; i++) {
            TransactionDTO transaction = transactionService.createTransaction(DemoConstants.DEMO_MEMBER_ID,validRequest);
            //sleep
            TimeUnit.MILLISECONDS.sleep(20);
            createdTransactions.put(transaction.getTransactionId(), transaction);
        }
        int page = 1;
        int size = 10;
        PageResult<TransactionDTO> pageResult = transactionService.pageListTransactions(page, size);
        assertNotNull(pageResult);
        assertEquals(size, pageResult.getPageSize());
        assertEquals(page, pageResult.getPage());
        assertTrue(pageResult.getTotal() >= count);
        assertEquals(1, pageResult.getTotalPages());
        assertTrue(count <= pageResult.getResults().size());
        //比对返回的顺序是transactionTime正序
        List<TransactionDTO> originResults = pageResult.getResults();
        List<TransactionDTO> sortedResults = originResults.stream().sorted(Comparator.comparing(TransactionDTO::getTransactionTime)).toList();
        for (int i = 0; i < count; i++) {
            assertEquals(sortedResults.get(i).getTransactionId(), originResults.get(i).getTransactionId());
        }

        //比对createdTransactions和返回的results
        originResults.forEach(transaction -> {
            assertEquals(createdTransactions.get(transaction.getTransactionId()), transaction);
        });

        //验证完清除
        createdTransactions.values().forEach(transaction -> {
            transactionService.deleteTransaction(transaction.getTransactionId(), DemoConstants.DEMO_MEMBER_ID);
        });
    }
    
    @Test
    void testWhenPageListTransactionsPartionsReturnSuccess() {
        Map<String,TransactionDTO> createdTransactions = new HashMap<>();

        int count = 21;
        for (int i = 0; i < count; i++) {
            TransactionDTO transaction = transactionService.createTransaction(DemoConstants.DEMO_MEMBER_ID,validRequest);
            createdTransactions.put(transaction.getTransactionId(), transaction);
        }
        
        // When retrieving with a specific page size
        int page = 1;
        int size = 10;
        PageResult<TransactionDTO> page1 = transactionService.pageListTransactions(page, size);
        PageResult<TransactionDTO> page2 = transactionService.pageListTransactions(page+1, size);
        PageResult<TransactionDTO> page3 = transactionService.pageListTransactions(page+2, size);
        
        assertEquals(size, page1.getResults().size());
        assertEquals(size, page2.getResults().size());
        assertEquals(true, page3.getResults().size() > 0);

        //验证完清除
        createdTransactions.values().forEach(transaction -> {
            transactionService.deleteTransaction(transaction.getTransactionId(), DemoConstants.DEMO_MEMBER_ID);
        });
    }
    
} 