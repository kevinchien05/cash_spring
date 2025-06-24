package com.example.cash.service.impl;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.cash.domain.Account;
import com.example.cash.domain.Category;
import com.example.cash.domain.Transaction;
import com.example.cash.dto.TransactionCategoryDTO;
import com.example.cash.dto.TransactionDTO;
import com.example.cash.exception.ResourceNotFoundException;
import com.example.cash.repository.AccountRepository;
import com.example.cash.repository.CategoryRepository;
import com.example.cash.repository.TransactionRepository;
import com.example.cash.service.TransactionService;

import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    @Override
    public List<TransactionCategoryDTO> getAllTransaction(Long accountID, Date start, Date end, String search) {
        start = ObjectUtils.isEmpty(start)?null:start;
        end = ObjectUtils.isEmpty(end)?null:end;
        search = ObjectUtils.isEmpty(search)?"%":"%"+search+"%";
        List<Transaction> transactions = transactionRepository.findAllByAccountIdAndDateRangeAndDescription(accountID, start, end, search);
        return transactions.stream().map(transaction -> {
            TransactionCategoryDTO dto = new TransactionCategoryDTO();
            dto.setId(transaction.getId());
            dto.setDate(transaction.getDate());
            dto.setDescription(transaction.getDescription());
            dto.setStatus(transaction.getStatus());
            dto.setTotal(transaction.getTotal());
            dto.setAccountId(transaction.getAccount().getId());
            dto.setCategoryName(transaction.getCategory().getName());
            return dto;
        }).collect(Collectors.toList()); 
    }

    @Override
    public String addNewTransaction(TransactionDTO dto, Long accountId) {
        Transaction transaction = new Transaction();
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account Not Found"));
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category Not Found")); 
        transaction.setDate(dto.getDate());
        transaction.setDescription(dto.getDescription());
        transaction.setStatus(dto.getStatus());
        transaction.setTotal(dto.getTotal());
        transaction.setAccount(account);
        transaction.setCategory(category);
        transactionRepository.save(transaction);
        return "Transaction Created";
    }

    @Override
    public String editTransaction(Long id, TransactionDTO dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String deleteTransaction(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
