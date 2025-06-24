package com.example.cash.service;


import java.sql.Date;
import java.util.List;

import com.example.cash.dto.TransactionCategoryDTO;
import com.example.cash.dto.TransactionDTO;

public interface TransactionService {
    
    public List<TransactionCategoryDTO> getAllTransaction(Long accountID, Date start, Date end, String search);
    
    public String addNewTransaction(TransactionDTO dto, Long accountId);

    public String editTransaction(Long id, TransactionDTO dto);

    public String deleteTransaction(Long id);
}
