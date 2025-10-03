package com.example.cash.service;


import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.cash.dto.TransactionCategoryDTO;
import com.example.cash.dto.TransactionDTO;
import com.example.cash.dto.TransactionDateDTO;
import com.example.cash.dto.TransactionJoinCategoryDTO;
import com.example.cash.dto.TransactionSumDTO;

public interface TransactionService {
    
    public List<TransactionCategoryDTO> getAllTransaction(Long accountID, Date start, Date end, String search);
    
    public TransactionCategoryDTO addNewTransaction(TransactionDTO dto, Long accountId);

    public TransactionCategoryDTO editTransaction(Long id, TransactionDTO dto);

    public String deleteTransaction(Long id);

    public BigDecimal getAccountBalance(Long accountID, Date start, Date end, Long categoryID);

    public BigDecimal getAccountOutcome (Long accountID, Date start, Date end);

    public List<TransactionSumDTO> getTransactionSumByCategory(Long accountID, Date start, Date end);

    public List<TransactionDateDTO> getTransactionGroupByDate(Long accountID, Date start, Date end, Boolean status);

    public List<TransactionJoinCategoryDTO> getTransactionCategoryName(Long accountID, Date start, Date end);

    public List<TransactionJoinCategoryDTO> getTransactionCategoryNameLimitOne(Long accountID, Date start, Date end);

    public Void importTransactionCSV(MultipartFile file, Long accountID, Boolean count) throws Exception;

    public BigDecimal getAccountIncomeDashboard(Long accountID, Date start, Date end);

}
