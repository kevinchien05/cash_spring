package com.example.cash.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.cash.domain.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    public static final String SELECT_FROM_BUDGET_ACCOUNTID_AND_DATE_RANGE = "SELECT * FROM budget WHERE account_id = :accountId AND DATE(date) BETWEEN :start AND :end";

    @Query(value = SELECT_FROM_BUDGET_ACCOUNTID_AND_DATE_RANGE, nativeQuery = true)
    List<Budget> findAllByAccount_IdAndDate(Long accountId, Date start, Date end);
}
