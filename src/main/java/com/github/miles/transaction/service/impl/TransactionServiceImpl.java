package com.github.miles.transaction.service.impl;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.github.miles.transaction.api.base.PageResult;
import com.github.miles.transaction.constants.TransactionType;
import com.github.miles.transaction.dto.TransactionDTO;
import com.github.miles.transaction.dto.request.CreateTransactionReq;
import com.github.miles.transaction.dto.request.UpdateTransactionReq;
import com.github.miles.transaction.exception.TransactionException.TransactionNoPermissionException;
import com.github.miles.transaction.exception.TransactionException.TransactionNotFoundException;
import com.github.miles.transaction.exception.TransactionException.TransactionParamInvalidException;
import com.github.miles.transaction.model.TransactionPO;
import com.github.miles.transaction.repository.TransactionRepository;
import com.github.miles.transaction.service.TransactionIdGenerator;
import com.github.miles.transaction.service.TransactionService;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 交易服务实现
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
    public TransactionDTO createTransaction(@Nonnull Long memberId,@Nonnull CreateTransactionReq request) {
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
    public void deleteTransaction(@Nonnull String transactionId, @Nonnull Long memberId) {
        log.info("Deleting transaction with transactionId: {},memberId: {}", transactionId, memberId);
        TransactionPO transaction = transactionRepository.findByTransactionId(transactionId);
        if(transaction == null){
            //ignore
            return;
        }
        //简单检查下只能删除自己的
        if(!transaction.getMemberId().equals(memberId)){
            throw new TransactionNoPermissionException();
        }
        int deleteCount = transactionRepository.delete(transactionId,memberId);
        log.info("Transaction deleted with transactionId: {},deleteCount: {}", transactionId, deleteCount);
    }

    @Override
    public TransactionDTO getTransaction(@Nonnull String transactionId, @Nonnull Long memberId) {
        log.info("Retrieving transaction with transactionId: {},memberId: {}", transactionId, memberId);

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
        // 页码从1开始
        page = Math.max(page, 1);
        // 每页大小>=10 且 <=100    
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
     * 将TransactionPO转换为TransactionDTO
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
     * 构建交易PO
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
     * 参数业务检查
     * 
     * @param request
     */
    private void checkParam(CreateTransactionReq request) {
        if(request == null){
            throw new TransactionParamInvalidException("Request is required");
        }
        // 参数业务检查
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