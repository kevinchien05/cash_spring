package com.example.cash.service.impl;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cash.domain.Account;
import com.example.cash.domain.Budget;
import com.example.cash.domain.Category;
import com.example.cash.dto.BudgetCategoryDTO;
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
    public List<BudgetCategoryDTO> getAllBudget(Long account_id, Date start, Date end) {
        List<Budget> dtos = budgetRepository.findAllByAccount_IdAndDate(account_id,start,end);
        return dtos.stream().map(budget -> {
            BudgetCategoryDTO dto = new BudgetCategoryDTO();
            dto.setId(budget.getId());
            dto.setTotal(budget.getTotal());
            dto.setDate(budget.getDate());
            dto.setAccountId(account_id);
            dto.setCategoryName(budget.getCategory().getName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public String addNewBudget(Long account_id, BudgetDTO dto) {
        Budget budget = new Budget();
        Account account = accountRepository.findById(account_id).orElseThrow(() -> new ResourceNotFoundException("account not found"));
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("category not found")); 
        budget.setTotal(dto.getTotal());
        budget.setDate(dto.getDate());
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
