// package com.github.miles.transaction.service;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;

// import java.math.BigDecimal;
// import java.util.List;
// import java.util.UUID;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;

// import com.github.miles.transaction.constants.TransactionType;
// import com.github.miles.transaction.dto.TransactionDTO;
// import com.github.miles.transaction.dto.request.CreateTransactionReq;
// import com.github.miles.transaction.exception.TransactionException.TransactionNotFoundException;

// /**
//  * 
//  * Test for the TransactionService
//  * 
//  * @author miles
//  * @version 1.0 
//  * @since 2025-03-13
//  */
// @SpringBootTest
// @ActiveProfiles("test")
// class TransactionServiceTest {

//     @Autowired
//     private TransactionService transactionService;
    
//     private CreateTransactionReq validRequest;
    
//     @BeforeEach
//     void setUp() {
//         // Create a sample transaction request for testing
//         validRequest = CreateTransactionReq.builder()
//                 .accountNumber("12345678")
//                 .amount(new BigDecimal("100.00"))
//                 .type(TransactionType.DEPOSIT)
//                 .description("Test transaction")
//                 .build();
        
//         // Create a transaction for testing updates and deletes
//         TransactionDTO response = transactionService.createTransaction(validRequest);
//         testId = response.getId();
//     }
    
//     @Test
//     void testCreateTransaction() {
//         // Given a valid transaction request
        
//         // When creating the transaction
//         TransactionDTO response = transactionService.createTransaction(validRequest);
        
//         // Then the transaction should be created successfully
//         assertNotNull(response);
//         assertNotNull(response.getId());
//         assertEquals(validRequest.getAccountNumber(), response.getAccountNumber());
//         assertEquals(validRequest.getAmount(), response.getAmount());
//         assertEquals(validRequest.getType(), response.getType());
//         assertEquals(validRequest.getDescription(), response.getDescription());
//     }
    
//     @Test
//     void testGetTransaction() {
//         // Given an existing transaction
        
//         // When retrieving the transaction
//         TransactionDTO response = transactionService.getTransaction(testId);
        
//         // Then the transaction should be returned successfully
//         assertNotNull(response);
//         assertEquals(testId, response.getId());
//     }
    
//     @Test
//     void testGetTransactionNotFound() {
//         // Given a non-existent transaction ID
//         UUID nonExistentId = UUID.randomUUID();
        
//         // When trying to retrieve the transaction
//         // Then an exception should be thrown
//         assertThrows(TransactionNotFoundException.class, () -> {
//             transactionService.getTransaction(nonExistentId);
//         });
//     }
    
//     @Test
//     void testUpdateTransaction() {
//         // Given an updated transaction request
//         CreateTransactionReq updateRequest = CreateTransactionReq.builder()
//                 .accountNumber("87654321")
//                 .amount(new BigDecimal("200.00"))
//                 .type(TransactionType.WITHDRAWAL)
//                 .description("Updated transaction")
//                 .build();
        
//         // When updating the transaction
//         TransactionDTO response = transactionService.updateTransaction(testId, updateRequest);
        
//         // Then the transaction should be updated successfully
//         assertNotNull(response);
//         assertEquals(testId, response.getId());
//         assertEquals(updateRequest.getAccountNumber(), response.getAccountNumber());
//         assertEquals(updateRequest.getAmount(), response.getAmount());
//         assertEquals(updateRequest.getType(), response.getType());
//         assertEquals(updateRequest.getDescription(), response.getDescription());
//     }
    
//     @Test
//     void testUpdateTransactionNotFound() {
//         // Given a non-existent transaction ID
//         UUID nonExistentId = UUID.randomUUID();
        
//         // When trying to update the transaction
//         // Then an exception should be thrown
//         assertThrows(TransactionNotFoundException.class, () -> {
//             transactionService.updateTransaction(nonExistentId, validRequest);
//         });
//     }
    
//     @Test
//     void testDeleteTransaction() {
//         // Given an existing transaction
//         TransactionDTO createdTransaction = transactionService.createTransaction(validRequest);
//         UUID idToDelete = createdTransaction.getId();
        
//         // When deleting the transaction
//         transactionService.deleteTransaction(idToDelete);
        
//         // Then the transaction should be deleted
//         assertThrows(TransactionNotFoundException.class, () -> {
//             transactionService.getTransaction(idToDelete);
//         });
//     }
    
//     @Test
//     void testDeleteTransactionNotFound() {
//         // Given a non-existent transaction ID
//         UUID nonExistentId = UUID.randomUUID();
        
//         // When trying to delete the transaction
//         // Then an exception should be thrown
//         assertThrows(TransactionNotFoundException.class, () -> {
//             transactionService.deleteTransaction(nonExistentId);
//         });
//     }
    
//     @Test
//     void testGetAllTransactions() {
//         // Given multiple transactions in the system
//         for (int i = 0; i < 5; i++) {
//             transactionService.createTransaction(validRequest);
//         }
        
//         // When retrieving all transactions with pagination
//         List<TransactionDTO> transactions = transactionService.getAllTransactions(0, 10);
        
//         // Then all transactions should be returned
//         assertNotNull(transactions);
//         // We should have at least 6 transactions (5 from this test + 1 from setUp)
//         assertEquals(true, transactions.size() >= 6);
//     }
    
//     @Test
//     void testGetAllTransactionsPagination() {
//         // Given more transactions than the page size
//         for (int i = 0; i < 15; i++) {
//             transactionService.createTransaction(validRequest);
//         }
        
//         // When retrieving with a specific page size
//         List<TransactionDTO> page1 = transactionService.getAllTransactions(0, 5);
//         List<TransactionDTO> page2 = transactionService.getAllTransactions(1, 5);
//         List<TransactionDTO> page3 = transactionService.getAllTransactions(2, 5);
        
//         // Then each page should have the correct number of transactions
//         assertEquals(5, page1.size());
//         assertEquals(5, page2.size());
//         // Page 3 might have fewer items depending on how many were created
//         assertEquals(true, page3.size() > 0);
//     }
// } 