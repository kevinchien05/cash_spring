package com.example.cash.service;

import java.sql.Date;
import java.util.List;

import com.example.cash.dto.BudgetCategoryDTO;
import com.example.cash.dto.BudgetDTO;

public interface BudgetService {
    public List<BudgetCategoryDTO> getAllBudget(Long account_id, Date start, Date end);

    public BudgetCategoryDTO addNewBudget(Long account_id, BudgetDTO dto);

    public BudgetCategoryDTO editBudget(Long id, BudgetDTO dto);

    public String deleteBudget(Long id);
}
