package com.example.cash.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cash.domain.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findAllByAccount_Id(Long accountId);
}
