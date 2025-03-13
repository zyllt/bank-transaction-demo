package com.hsbc.demo.transaction.controller;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hsbc.demo.transaction.api.base.ApiCode;
import com.hsbc.demo.transaction.api.base.BaseResponse;
import com.hsbc.demo.transaction.api.base.PageResult;
import com.hsbc.demo.transaction.api.base.helper.ResponseHelper;
import com.hsbc.demo.transaction.constants.DemoConstants;
import com.hsbc.demo.transaction.dto.TransactionDTO;
import com.hsbc.demo.transaction.dto.request.CreateTransactionReq;
import com.hsbc.demo.transaction.dto.request.UpdateTransactionReq;
import com.hsbc.demo.transaction.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Transaction Controller
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@RestController
@RequestMapping(TransactionController.API_TRANSACTIONS)
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    public static final String API_TRANSACTIONS = "/api/transactions";

    private final TransactionService transactionService;

    /**
     * Create transaction
     * 
     * @param memberId Treated as the requesting user ID
     * @param request  Create transaction request
     * @return Transaction DTO
     */
    @PostMapping
    public Mono<BaseResponse<TransactionDTO>> createTransaction(
            @RequestParam(name = "@MemberId", defaultValue = DemoConstants.DEMO_MEMBER_ID_STR, required = false) Long memberId,
            @Valid @RequestBody CreateTransactionReq request) {
        return Mono.defer(() -> {
            if (memberId == null || memberId <= 0) {
                // Need memberId as request parameter for logged-in user
                return Mono.just(ResponseHelper.failure(ApiCode.ERROR_FORBIDDEN.getCode(),
                        "need @memberId as request param for login user"));
            }
            TransactionDTO dto = transactionService.createTransaction(memberId, request);
            return Mono.just(new BaseResponse<>(dto));
        });
    }

    /**
     * Update transaction
     * 
     * @param transactionId Transaction ID
     * @param memberId      Treated as the requesting user ID
     * @param request       Update transaction request
     * @return Transaction DTO
     */
    @PutMapping("/{transactionId}")
    public Mono<BaseResponse<TransactionDTO>> updateTransaction(
            @PathVariable String transactionId,
            @RequestParam(name = "@MemberId", defaultValue = DemoConstants.DEMO_MEMBER_ID_STR, required = false) Long memberId,
            @Valid @RequestBody UpdateTransactionReq request) {
        return Mono.defer(() -> {
            if (memberId == null || memberId <= 0) {
                return Mono.just(ResponseHelper.failure(ApiCode.ERROR_FORBIDDEN.getCode(),
                        "need @memberId as request param for login user"));
            }
            TransactionDTO dto = transactionService.updateTransaction(transactionId, request);
            return Mono.just(new BaseResponse<>(dto));
        });
    }

    /**
     * Delete transaction
     * 
     * @param transactionId Transaction ID
     * @param memberId      Treated as the requesting user ID
     * @return Empty
     */
    @DeleteMapping("/{transactionId}")
    public Mono<BaseResponse<Void>> deleteTransaction(
            @PathVariable String transactionId,
            @RequestParam(name = "@MemberId", defaultValue = DemoConstants.DEMO_MEMBER_ID_STR, required = false) Long memberId) {
        return Mono.defer(() -> {
            if (memberId == null || memberId <= 0) {
                return Mono.just(ResponseHelper.failure(ApiCode.ERROR_FORBIDDEN.getCode(),
                        "need @memberId as request param for login user"));
            }
            transactionService.deleteTransaction(transactionId, memberId);
            return Mono.just(new BaseResponse<>());
        });
    }

    /**
     * Get transaction
     * 
     * @param transactionId Transaction ID
     * @param memberId      Treated as the requesting user ID
     * @return Transaction DTO
     */
    @GetMapping("/{transactionId}")
    public Mono<BaseResponse<TransactionDTO>> getTransaction(@PathVariable String transactionId,
            @RequestParam(name = "@MemberId", defaultValue = DemoConstants.DEMO_MEMBER_ID_STR, required = false) Long memberId) {
        return Mono.defer(() -> {
            if (memberId == null || memberId <= 0) {
                return Mono.just(ResponseHelper.failure(ApiCode.ERROR_FORBIDDEN.getCode(),
                        "need @memberId as request param for login user"));
            }
            TransactionDTO response = transactionService.getTransaction(transactionId, memberId);
            return Mono.just(new BaseResponse<>(response));
        });
    }

    /**
     * Get all transactions with pagination
     * 
     * @param page Page number
     * @param size Page size
     * @return Transaction list
     */
    @GetMapping
    public Mono<BaseResponse<PageResult<TransactionDTO>>> pageListTransactions(
            @Range(min = 1, max = 500, message = "page must be between 1 and 500") @RequestParam(defaultValue = "1") int page,
            @Range(min = 10, max = 100, message = "pageSize must be between 10 and 100") @RequestParam(defaultValue = "10") int size) {
        return Mono.defer(() -> {
            PageResult<TransactionDTO> transactions = transactionService.pageListTransactions(page, size);
            return Mono.just(new BaseResponse<>(transactions));
        });
    }
}