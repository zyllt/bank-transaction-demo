package com.github.miles.transaction.controller;

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

import com.github.miles.transaction.api.base.ApiCode;
import com.github.miles.transaction.api.base.BaseResponse;
import com.github.miles.transaction.api.base.PageResult;
import com.github.miles.transaction.api.base.helper.ResponseHelper;
import com.github.miles.transaction.constants.DemoConstants;
import com.github.miles.transaction.dto.TransactionDTO;
import com.github.miles.transaction.dto.request.CreateTransactionReq;
import com.github.miles.transaction.dto.request.UpdateTransactionReq;
import com.github.miles.transaction.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 交易控制器
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
     * 创建交易
     * 
     * @param memberId 当作请求的用户ID
     * @param request  创建交易请求
     * @return 交易DTO
     */
    @PostMapping
    public Mono<BaseResponse<TransactionDTO>> createTransaction(
            @RequestParam(name = "@MemberId", defaultValue = DemoConstants.DEMO_MEMBER_ID_STR, required = false) Long memberId,
            @Valid @RequestBody CreateTransactionReq request) {
        return Mono.defer(() -> {
            if (memberId == null || memberId <= 0) {
                // 需要memberId作为请求参数，当作登陆用户
                return Mono.just(ResponseHelper.failure(ApiCode.ERROR_FORBIDDEN.getCode(),
                        "need @memberId as request param for login user"));
            }
            TransactionDTO dto = transactionService.createTransaction(memberId, request);
            return Mono.just(new BaseResponse<>(dto));
        });
    }

    /**
     * 更新交易
     * 
     * @param transactionId 交易ID
     * @param memberId      当作请求的用户ID
     * @param request       更新交易请求
     * @return 交易DTO
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
     * 删除交易
     * 
     * @param transactionId 交易ID
     * @param memberId      当作请求的用户ID
     * @return 空
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
     * 获取交易
     * 
     * @param transactionId 交易ID
     * @param memberId      当作请求的用户ID
     * @return 交易DTO
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
     * 分页获取所有交易
     * 
     * @param page 页码
     * @param size 每页大小
     * @return 交易列表
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