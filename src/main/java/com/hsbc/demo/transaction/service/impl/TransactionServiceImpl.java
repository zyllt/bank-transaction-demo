package com.hsbc.demo.transaction.service.impl;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.hsbc.demo.transaction.api.base.PageResult;
import com.hsbc.demo.transaction.config.CacheConfig;
import com.hsbc.demo.transaction.constants.TransactionType;
import com.hsbc.demo.transaction.dto.TransactionDTO;
import com.hsbc.demo.transaction.dto.request.CreateTransactionReq;
import com.hsbc.demo.transaction.dto.request.UpdateTransactionReq;
import com.hsbc.demo.transaction.exception.TransactionException.TransactionNoPermissionException;
import com.hsbc.demo.transaction.exception.TransactionException.TransactionNotFoundException;
import com.hsbc.demo.transaction.exception.TransactionException.TransactionParamInvalidException;
import com.hsbc.demo.transaction.model.TransactionPO;
import com.hsbc.demo.transaction.repository.TransactionRepository;
import com.hsbc.demo.transaction.service.TransactionIdGenerator;
import com.hsbc.demo.transaction.service.TransactionService;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Transaction Service Implementation
 * 
 * @author miles.zeng
 * @since 2025-03-12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionIdGenerator transactionIdGenerator;

    private final TransactionRepository transactionRepository;

    @Override
    @CachePut(value = CacheConfig.TRANSACTION_CACHE_NAME, key = "#result.transactionId")
    public TransactionDTO createTransaction(@Nonnull Long memberId, @Nonnull CreateTransactionReq request) {
        if(request == null){
            throw new NullPointerException("Request is required");
        }
        log.info(
                "Creating new transaction start,memberId: {},accountId: {},opponentAccountId: {},amount: {},type: {},description: {}",
                memberId, request.getAccountId(), request.getOpponentAccountId(), request.getAmount(),
                request.getTransactionType(), request.getDescription());
        checkParam(request);
        var po = transactionRepository.save(buildTransactionPO(memberId, request));
        log.info("Transaction created with id: {},transactionId: {}", po.getId(), po.getTransactionId());
        return mapToResponse(po);
    }

    @Override
    @Caching(
        evict = { @CacheEvict(value = CacheConfig.TRANSACTION_CACHE_NAME, key = "#transactionId") },
        put = { @CachePut(value = CacheConfig.TRANSACTION_CACHE_NAME, key = "#result.transactionId") }
    )
    public TransactionDTO updateTransaction(@Nonnull String transactionId, @Nonnull UpdateTransactionReq request) {
        if(request == null){
            throw new NullPointerException("Request is required");
        }
        log.info("Updating transaction with transactionId: {},request: {}", transactionId, request);
        if(StringUtils.isBlank(transactionId)){
            throw new TransactionParamInvalidException("TransactionId is required");
        }
        checkParam(request);
        TransactionPO transaction = buildUpdateTransactionPO(transactionId, request);

        int updateCount = transactionRepository.update(transaction);
        log.info("Transaction updated with transactionId: {},updateCount: {}", transactionId, updateCount);

        return mapToResponse(transaction);
    }

    @Override
    @CacheEvict(value = CacheConfig.TRANSACTION_CACHE_NAME, key = "#transactionId")
    public void deleteTransaction(@Nonnull String transactionId, @Nonnull Long memberId) {
        log.info("Deleting transaction with transactionId: {},memberId: {}", transactionId, memberId);
        TransactionPO transaction = transactionRepository.findByTransactionId(transactionId);
        if(transaction == null){
            //ignore
            return;
        }
        //Simple check to ensure users can only delete their own transactions
        if(!transaction.getMemberId().equals(memberId)){
            throw new TransactionNoPermissionException();
        }
        int deleteCount = transactionRepository.delete(transactionId,memberId);
        log.info("Transaction deleted with transactionId: {},deleteCount: {}", transactionId, deleteCount);
    }

    @Override
    @Cacheable(
        value = CacheConfig.TRANSACTION_CACHE_NAME, 
        key = "#transactionId",
        unless = "#result == null"
    )
    public TransactionDTO getTransaction(@Nonnull String transactionId, @Nonnull Long memberId) {
        log.info("Cache miss! Retrieving transaction with transactionId: {}, memberId: {}", transactionId, memberId);
        TransactionPO transaction = transactionRepository.findByTransactionId(transactionId);
        if (transaction == null) {
            log.error("Transaction not found with transactionId: {}", transactionId);
            throw new TransactionNotFoundException("Transaction not found with transactionId: " + transactionId);
        }
        return mapToResponse(transaction);
    }

    @Override
    public PageResult<TransactionDTO> pageListTransactions(int page, int size) {
        log.info("Retrieving all transactions with pagination - page: {}, size: {}", page, size);
        // Page number starts from 1
        page = Math.max(page, 1);
        // Page size >= 10 and <= 100    
        size = Math.max(size, 10);
        size = Math.min(size, 100);
        var pageResult = new PageResult<TransactionDTO>();
        pageResult.setPage(page);
        pageResult.setPageSize(size);
        pageResult.setTotal(transactionRepository.count());
        pageResult.setTotalPages((int)Math.ceil((double)transactionRepository.count() / size));
        pageResult.setResults(transactionRepository.pageList(page, size).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList()));
        return pageResult;
    }

    /**
     * Convert TransactionPO to TransactionDTO
     * 
     * @param transaction
     * @return
     */
    private TransactionDTO mapToResponse(TransactionPO transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setTransactionId(transaction.getTransactionId());
        dto.setMemberId(transaction.getMemberId());
        dto.setAccountId(transaction.getAccountId());
        dto.setOpponentAccountId(transaction.getOpponentAccountId());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setDescription(transaction.getDescription());
        dto.setTransactionTime(transaction.getTransactionTime());
        dto.setUpdatedTime(transaction.getUpdatedTime());
        return dto;
    }

    private TransactionPO buildUpdateTransactionPO(String transactionId, UpdateTransactionReq request) {
        TransactionPO transaction = transactionRepository.findByTransactionId(transactionId);
        if (transaction == null) {
            log.error("Transaction not found with transactionId: {}", transactionId);
            throw new TransactionNotFoundException("Transaction not found with transactionId: " + transactionId);
        }
        transaction.setAccountId(request.getAccountId());
        transaction.setOpponentAccountId(request.getOpponentAccountId());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setDescription(request.getDescription());
        transaction.setUpdatedTime(System.currentTimeMillis());
        return transaction;
    }

    /**
     * Build transaction PO
     * 
     * @param memberId
     * @param request
     * @return
     */
    private TransactionPO buildTransactionPO(Long memberId, CreateTransactionReq request) {
        var po = new TransactionPO();
        po.setTransactionId(transactionIdGenerator.nextTransactionId());
        po.setMemberId(memberId);
        po.setAccountId(request.getAccountId());
        po.setOpponentAccountId(request.getOpponentAccountId());
        po.setAmount(request.getAmount());
        po.setTransactionType(request.getTransactionType());
        po.setDescription(request.getDescription());
        po.setTransactionTime(System.currentTimeMillis());
        po.setUpdatedTime(po.getTransactionTime());
        return po;
    }

    /**
     * Business parameter validation
     * 
     * @param request
     */
    private void checkParam(CreateTransactionReq request) {
        if(request == null){
            throw new TransactionParamInvalidException("Request is required");
        }
        // Business parameter validation
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionParamInvalidException("Amount must be greater than 0");
        }
        if (StringUtils.isBlank(request.getAccountId())) {
            throw new TransactionParamInvalidException("AccountId is required");
        }
        if (StringUtils.isBlank(request.getOpponentAccountId())) {
            throw new TransactionParamInvalidException("OpponentAccountId is required");
        }
        if (request.getTransactionType() == null || TransactionType.from(request.getTransactionType()).isEmpty()) {
            throw new TransactionParamInvalidException("TransactionType is required");
        }
        if (StringUtils.isBlank(request.getDescription())) {
            throw new TransactionParamInvalidException("Description is required");
        }
    }
}