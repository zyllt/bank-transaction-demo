package com.hsbc.demo.transaction.controller;

import static com.hsbc.demo.transaction.controller.TransactionController.API_TRANSACTIONS;
import static com.hsbc.demo.transaction.utils.AssertTestUtils.assertBaseResponse;
import static com.hsbc.demo.transaction.utils.AssertTestUtils.assertBaseResponsePageResult;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.hsbc.demo.transaction.BaseTest;
import com.hsbc.demo.transaction.api.base.ApiCode;
import com.hsbc.demo.transaction.api.base.BaseResponse;
import com.hsbc.demo.transaction.api.base.PageResult;
import com.hsbc.demo.transaction.constants.DemoConstants;
import com.hsbc.demo.transaction.dto.TransactionDTO;
import com.hsbc.demo.transaction.dto.request.CreateTransactionReq;
import com.hsbc.demo.transaction.dto.request.UpdateTransactionReq;
import com.hsbc.demo.transaction.exception.TransactionException.TransactionNotFoundException;
import com.hsbc.demo.transaction.exception.TransactionException.TransactionParamInvalidException;
import com.hsbc.demo.transaction.service.TransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Tests for the TransactionController
 * 
 * @author miles
 * @version 1.0
 * @since 2025-03-13
 */
@WebFluxTest(TransactionController.class)
@TestMethodOrder(OrderAnnotation.class)
class TransactionControllerTest extends BaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TransactionService transactionService;

    @Test
    void testWhenCreateTransactionSuccess() {
        when(transactionService.createTransaction(anyLong(), any(CreateTransactionReq.class)))
                .thenReturn(mockDTO);
        webTestClient.post()
                .uri(API_TRANSACTIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<BaseResponse<TransactionDTO>>() {
                })
                .value(assertBaseResponse(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), mockDTO));
    }

    // TransactionParamInvalidException
    @Test
    void testWhenCreateTransactionParamInvalidReturnError() {
        TransactionParamInvalidException ex = new TransactionParamInvalidException("Invalid request");
        when(transactionService.createTransaction(anyLong(), any(CreateTransactionReq.class)))
                .thenThrow(ex);
        webTestClient.post()
                .uri(API_TRANSACTIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<BaseResponse<TransactionDTO>>() {
                })
                .value(assertBaseResponse(ex.getCode(), ex.getMessage()));
    }

    // .jsonPath("$.code").isEqualTo(mockSuccessRsp.getCode())
    // .jsonPath("$.msg").isEqualTo(mockSuccessRsp.getMsg())
    // .jsonPath("$.data").isEqualTo(mockSuccessRsp.getData());

    @Test
    void testGetTransactionSuccess() {
        when(transactionService.getTransaction(eq(testTransactionId), any(Long.class))).thenReturn(mockDTO);

        webTestClient.get()
                .uri(API_TRANSACTIONS + "/{id}", testTransactionId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<BaseResponse<TransactionDTO>>() {
                })
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
                .expectBody(new ParameterizedTypeReference<BaseResponse<TransactionDTO>>() {
                })
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
                .expectBody(new ParameterizedTypeReference<BaseResponse<TransactionDTO>>() {
                })
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
                .expectBody(new ParameterizedTypeReference<BaseResponse<PageResult<TransactionDTO>>>() {
                })
                .value(assertBaseResponsePageResult(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(),
                        mockPageResult));
    }

    @Test
    void performanceTest_CreateTransaction() {
        Flux.range(1, 100)
                .flatMap(i -> {
                    return webTestClient.mutate().responseTimeout(java.time.Duration.ofSeconds(30)).build()
                            .post()
                            .uri(uriBuilder -> uriBuilder
                                    .path(TransactionController.API_TRANSACTIONS)
                                    .queryParam("@MemberId", DemoConstants.DEMO_MEMBER_ID_STR)
                                    .build())
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Mono.just(validRequest), CreateTransactionReq.class)
                            .exchange()
                            .returnResult(BaseResponse.class)
                            .getResponseBody();
                })
                .collectList()
                .block();
    }

    @Test
    void performanceTest_PageListTransactions() {
        webTestClient.mutate().responseTimeout(java.time.Duration.ofSeconds(30)).build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(TransactionController.API_TRANSACTIONS)
                        .queryParam("page", 1)
                        .queryParam("size", 10)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo(ApiCode.SUCCESS.getCode());
    }
    

}