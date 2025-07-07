package com.example.cash.service.impl;

import java.math.BigDecimal;
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
import com.example.cash.dto.TransactionSumDTO;
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
        start = ObjectUtils.isEmpty(start) ? null : start;
        end = ObjectUtils.isEmpty(end) ? null : end;
        search = ObjectUtils.isEmpty(search) ? "%" : "%" + search + "%";
        List<Transaction> transactions = transactionRepository.findAllByAccountIdAndDateRangeAndDescription(accountID, start, end, search);
        return transactions.stream().map(transaction -> {
            TransactionCategoryDTO dto = new TransactionCategoryDTO();
            dto.setId(transaction.getId());
            dto.setDate(transaction.getDate());
            dto.setDescription(transaction.getDescription());
            if (transaction.getStatus()) {
                dto.setStatus("In");
            } else {
                dto.setStatus("Out");
            }
            dto.setTotal(transaction.getTotal());
            dto.setAccountId(transaction.getAccount().getId());
            dto.setCategoryName(transaction.getCategory().getName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
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
        BigDecimal total;
        //true anggap masuk
        if (dto.getStatus()) {
            total = account.getBalance().add(dto.getTotal());
        } else {
            total = account.getBalance().subtract(dto.getTotal());
        }
        account.setBalance(total);
        accountRepository.save(account);
        return "Transaction Created";
    }

    @Override
    public String editTransaction(Long id, TransactionDTO dto) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction Not Found"));
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category Not Found"));
        Account account = accountRepository.findById(transaction.getAccount().getId()).orElseThrow(() -> new ResourceNotFoundException("Account Not Found"));
        // BigDecimal total = account.getBalance().add(transaction.getTotal());
        BigDecimal total = account.getBalance();
        if (transaction.getStatus()) {
            total = total.subtract(transaction.getTotal());
        } else {
            total = total.add(transaction.getTotal());
        }
        transaction.setDate(dto.getDate());
        transaction.setDescription(dto.getDescription());
        transaction.setStatus(dto.getStatus());
        transaction.setTotal(dto.getTotal());
        transaction.setCategory(category);
        transactionRepository.save(transaction);
        //true anggap masuk
        if (dto.getStatus()) {
            total = total.add(dto.getTotal());
        } else {
            total = total.subtract(dto.getTotal());
        }
        account.setBalance(total);
        accountRepository.save(account);
        return "Transaction Edited";
    }

    //transaksi berhasil dihapus akan tetapi total balance pada account masi belum berubah
    @Override
    public String deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction Not Found"));
        Account account = accountRepository.findById(transaction.getAccount().getId()).orElseThrow(() -> new ResourceNotFoundException("Account Not Found"));
        BigDecimal total = account.getBalance();
        //true anggap masuk
        if (transaction.getStatus()) {
            total = total.subtract(transaction.getTotal());
        } else {
            total = total.add(transaction.getTotal());
        }
        transactionRepository.delete(transaction);
        account.setBalance(total);
        accountRepository.save(account);
        return "Transaction deleted";
    }

    @Transactional
    @Override
    public BigDecimal getAccountBalance(Long accountID, Date start, Date end, Long categoryID) {
        start = ObjectUtils.isEmpty(start) ? null : start;
        end = ObjectUtils.isEmpty(end) ? null : end;
        List<Transaction> transactions = transactionRepository.findIncomeByAccountIDAndMonthAndCategoryID(accountID, start, end, categoryID);
        BigDecimal total = transactions.stream().map(Transaction::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }

    @Override
    public List<TransactionSumDTO> getTransactionSumByCategory(Long accountID, Date start, Date end) {
        List<TransactionSumDTO> transactions = transactionRepository.findTransactionSumGroupByCategory(accountID, start, end);
        return transactions;
    }

}
