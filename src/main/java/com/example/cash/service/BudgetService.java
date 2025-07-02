package com.example.cash.service;

import java.util.List;

import com.example.cash.dto.BudgetDTO;

public interface BudgetService {
    public List<BudgetDTO> getAllBudget(Long account_id);

    public String addNewBudget(Long account_id, BudgetDTO dto);

    public String editBudget(Long id, BudgetDTO dto);

    public String deleteBudget(Long id);
}
