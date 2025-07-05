package com.example.cash.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.cash.domain.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    public static final String SELECT_FROM_TRANSACTION_WHERE_ACCOUNT_ID_ACCOUNT_ID_AND_DATE_DATE_BETWEEN_START_AND_END_AND_DESCRIPTION_ILIKE_DESCRIPTION = "SELECT * FROM transaction WHERE account_id = :accountId AND DATE(date) BETWEEN :start AND :end AND description ILIKE %:description%";

    public static final String SELECT_FROM_TRANSACTION_BALANCE = "SELECT * FROM transaction WHERE account_id = :accountId AND DATE(date) BETWEEN :start AND :end AND category_id = :categoryId";

    List<Transaction> findAllByAccount_IdAndDateAndDescription(Long accountId, Date date, String description);

    @Query(value = SELECT_FROM_TRANSACTION_WHERE_ACCOUNT_ID_ACCOUNT_ID_AND_DATE_DATE_BETWEEN_START_AND_END_AND_DESCRIPTION_ILIKE_DESCRIPTION, nativeQuery = true)
    List<Transaction> findAllByAccountIdAndDateRangeAndDescription(Long accountId, Date start, Date end, String description);

    @Query(value = SELECT_FROM_TRANSACTION_BALANCE, nativeQuery = true)
    List<Transaction> findIncomeByAccountIDAndMonthAndCategoryID(Long accountId, Date start, Date end, Long categoryId);
}
