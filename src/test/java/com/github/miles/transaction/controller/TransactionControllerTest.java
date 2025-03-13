package com.github.miles.transaction.controller;

import static com.github.miles.transaction.controller.TransactionController.API_TRANSACTIONS;
import static com.github.miles.transaction.utils.AssertTestUtils.assertBaseResponse;
import static com.github.miles.transaction.utils.AssertTestUtils.assertBaseResponsePageResult;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.miles.transaction.BaseTest;
import com.github.miles.transaction.api.base.ApiCode;
import com.github.miles.transaction.api.base.BaseResponse;
import com.github.miles.transaction.api.base.PageResult;
import com.github.miles.transaction.dto.TransactionDTO;
import com.github.miles.transaction.dto.request.CreateTransactionReq;
import com.github.miles.transaction.dto.request.UpdateTransactionReq;
import com.github.miles.transaction.exception.TransactionException.TransactionNotFoundException;
import com.github.miles.transaction.exception.TransactionException.TransactionParamInvalidException;
import com.github.miles.transaction.service.TransactionService;

/**
 * Tests for the TransactionController
 * 
 * @author miles
 * @version 1.0
 * @since 2025-03-13
 */
@WebFluxTest(TransactionController.class)
class TransactionControllerTest extends BaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TransactionService transactionService;


    @Test
    void testCreateTransactionSuccess() {
        when(transactionService.createTransaction(anyLong(), any(CreateTransactionReq.class)))
                .thenReturn(mockDTO);
        webTestClient.post()
                .uri(API_TRANSACTIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<BaseResponse<TransactionDTO>>() {})
                .value(assertBaseResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), mockDTO));
    }

    //TransactionParamInvalidException
    @Test
    void testCreateTransactionParamInvalid() {
        TransactionParamInvalidException ex = new TransactionParamInvalidException("Invalid request");
        when(transactionService.createTransaction(anyLong(), any(CreateTransactionReq.class)))
                .thenThrow(ex);
        webTestClient.post()
                .uri(API_TRANSACTIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<BaseResponse<TransactionDTO>>() {})
                .value(assertBaseResponse(ex.getCode(), ex.getMessage()));
    }


    // .jsonPath("$.code").isEqualTo(mockSuccessRsp.getCode())
    //             .jsonPath("$.msg").isEqualTo(mockSuccessRsp.getMsg())
    //             .jsonPath("$.data").isEqualTo(mockSuccessRsp.getData());

    @Test
    void testGetTransactionSuccess() {
        when(transactionService.getTransaction(eq(testTransactionId), any(Long.class))).thenReturn(mockDTO);

        webTestClient.get()
                .uri(API_TRANSACTIONS + "/{id}", testTransactionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<BaseResponse<TransactionDTO>>() {})
                .value(assertBaseResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), mockDTO));
    }

    @Test
    void testGetTransactionNotFound() {
        TransactionNotFoundException ex = new TransactionNotFoundException("Not found");
        when(transactionService.getTransaction(eq(testTransactionId), any(Long.class))).thenThrow(ex);

        webTestClient.get()
                .uri(API_TRANSACTIONS + "/{id}", testTransactionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<BaseResponse<TransactionDTO>>() {})
                .value(assertBaseResponse(ex.getCode(), ex.getMessage()));
    }

    @Test
    void testUpdateTransactionSuccess() {
        when(transactionService.updateTransaction(eq(testTransactionId), any(UpdateTransactionReq.class)))
                .thenReturn(mockDTO);

        webTestClient.put()
                .uri(API_TRANSACTIONS + "/{id}", testTransactionId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<BaseResponse<TransactionDTO>>() {})
                .value(assertBaseResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), mockDTO));
    }

    @Test
    void testDeleteTransactionSuccess() {
        webTestClient.delete()
                .uri(API_TRANSACTIONS + "/{id}", testTransactionId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testPageListTransactionsSuccess() {
        List<TransactionDTO> mockResponses = Arrays.asList(mockDTO, mockDTO);
        PageResult<TransactionDTO> mockPageResult = new PageResult<>();
        mockPageResult.setResults(mockResponses);
        mockPageResult.setPage(1);
        mockPageResult.setPageSize(10);
        mockPageResult.setTotal(2);
        mockPageResult.setTotalPages(1);
        when(transactionService.pageListTransactions(1, 10)).thenReturn(mockPageResult);

        webTestClient.get()
                .uri(API_TRANSACTIONS + "?page=1&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<BaseResponse<PageResult<TransactionDTO>>>() {})
                .value(assertBaseResponsePageResult(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), mockPageResult));
    }

}