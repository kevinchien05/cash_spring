package com.example.cash.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cash.domain.Account;
import com.example.cash.domain.Budget;
import com.example.cash.domain.Category;
import com.example.cash.dto.BudgetDTO;
import com.example.cash.exception.ResourceNotFoundException;
import com.example.cash.repository.AccountRepository;
import com.example.cash.repository.BudgetRepository;
import com.example.cash.repository.CategoryRepository;
import com.example.cash.service.BudgetService;

import jakarta.transaction.Transactional;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    @Override
    public List<BudgetDTO> getAllBudget(Long account_id) {
        List<Budget> dtos = budgetRepository.findAllByAccount_Id(account_id);
        return dtos.stream().map(budget -> {
            BudgetDTO dto = new BudgetDTO();
            dto.setId(budget.getId());
            dto.setTotal(budget.getTotal());
            dto.setAccountId(account_id);
            dto.setCategoryId(budget.getCategory().getId());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public String addNewBudget(Long account_id, BudgetDTO dto) {
        Budget budget = new Budget();
        Account account = accountRepository.findById(account_id).orElseThrow(() -> new ResourceNotFoundException("account not found"));
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("category not found")); 
        budget.setTotal(dto.getTotal());
        budget.setAccount(account);
        budget.setCategory(category);
        budgetRepository.save(budget);
        return "Budget Created";
    }


    @Override
    public String editBudget(Long id, BudgetDTO dto) {
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Budget Not Found"));
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("category not found"));
        budget.setTotal(dto.getTotal());
        budget.setCategory(category);
        budgetRepository.save(budget);
        return "Budget Edited"; 
    }

    @Override
    public String deleteBudget(Long id) {
        budgetRepository.deleteById(id);
        return "Budget Deleted";
    }

}
