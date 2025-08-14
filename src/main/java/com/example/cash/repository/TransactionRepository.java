package com.example.cash.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.cash.domain.Transaction;
import com.example.cash.dto.TransactionDateDTO;
import com.example.cash.dto.TransactionJoinCategoryDTO;
import com.example.cash.dto.TransactionSumDTO;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    //need change this is for pgsql
    // public static final String SELECT_FROM_TRANSACTION_WHERE_ACCOUNT_ID_ACCOUNT_ID_AND_DATE_DATE_BETWEEN_START_AND_END_AND_DESCRIPTION_ILIKE_DESCRIPTION = "SELECT * FROM transaction WHERE account_id = :accountId AND DATE(date) BETWEEN :start AND :end AND description ILIKE %:description% ORDER BY date ASC";
    //for mysql no ilike expression
    public static final String SELECT_FROM_TRANSACTION_WHERE_ACCOUNT_ID_ACCOUNT_ID_AND_DATE_DATE_BETWEEN_START_AND_END_AND_DESCRIPTION_ILIKE_DESCRIPTION = "SELECT * FROM transaction WHERE account_id = :accountId AND DATE(date) BETWEEN :start AND :end AND description LIKE %:description% ORDER BY date ASC";

    public static final String SELECT_FROM_TRANSACTION_BALANCE = "SELECT * FROM transaction WHERE account_id = :accountId AND DATE(date) BETWEEN :start AND :end AND category_id = :categoryId AND status = true";

    public static final String SELECT_FROM_TRANSACTION_OUTCOME = "SELECT * FROM transaction WHERE account_id = :accountId AND DATE(date) BETWEEN :start AND :end AND status = false";

    public static final String SELECT_FROM_TRANSACTION_GROUP_BY_CATEGORY = "SELECT category_id AS categoryId, SUM(total) AS total FROM transaction WHERE account_id = :accountId AND DATE(date) BETWEEN :start AND :end AND status = false GROUP BY category_id";

    public static final String SELECT_FROM_TRANSACTION_GROUP_BY_DATE = "SELECT date, SUM(total) AS total FROM transaction WHERE account_id = :accountId AND DATE(date) BETWEEN :start AND :end AND status = :status GROUP BY date ORDER BY date ASC";

    public static final String SELECT_FROM_TRANSACTION_JOIN_CATEGORY = "SELECT category.id AS categoryId, category.name AS categoryName, SUM(transaction.total) AS total FROM transaction JOIN category on transaction.category_id = category.id WHERE transaction.account_id = :accountId AND DATE(transaction.date) BETWEEN :start AND :end AND status = false GROUP BY category.id";

    public static final String SELECT_FROM_TRANSACTION_JOIN_CATEGORY_LIMIT_ONE = "SELECT category.id AS categoryId, category.name AS categoryName, SUM(transaction.total) AS total FROM transaction JOIN category on transaction.category_id = category.id WHERE transaction.account_id = :accountId AND DATE(transaction.date) BETWEEN :start AND :end AND status = false GROUP BY category.id ORDER BY SUM(transaction.total) DESC LIMIT 1";

    List<Transaction> findAllByAccount_IdAndDateAndDescription(Long accountId, Date date, String description);

    //Need to change when migrate from pgsql to mysql
    @Query(value = SELECT_FROM_TRANSACTION_WHERE_ACCOUNT_ID_ACCOUNT_ID_AND_DATE_DATE_BETWEEN_START_AND_END_AND_DESCRIPTION_ILIKE_DESCRIPTION, nativeQuery = true)
    List<Transaction> findAllByAccountIdAndDateRangeAndDescription(Long accountId, Date start, Date end, String description);

    @Query(value = SELECT_FROM_TRANSACTION_BALANCE, nativeQuery = true)
    List<Transaction> findIncomeByAccountIDAndMonthAndCategoryID(Long accountId, Date start, Date end, Long categoryId);

    @Query(value = SELECT_FROM_TRANSACTION_GROUP_BY_CATEGORY, nativeQuery = true)
    List<TransactionSumDTO> findTransactionSumGroupByCategory(Long accountId, Date start, Date end);

    @Query(value = SELECT_FROM_TRANSACTION_OUTCOME, nativeQuery = true)
    List<Transaction> findOutcomeByAccountIDAndMonth(Long accountId, Date start, Date end);

    @Query(value = SELECT_FROM_TRANSACTION_GROUP_BY_DATE, nativeQuery = true)
    List<TransactionDateDTO> findTransactionGroupByDate(Long accountId, Date start, Date end, Boolean status);

    @Query(value = SELECT_FROM_TRANSACTION_JOIN_CATEGORY, nativeQuery = true)
    List<TransactionJoinCategoryDTO> findTransactionJoinCategory(Long accountId, Date start, Date end);
    
    @Query(value = SELECT_FROM_TRANSACTION_JOIN_CATEGORY_LIMIT_ONE, nativeQuery = true)
    List<TransactionJoinCategoryDTO> findTransactionJoinCategoryLimitOne(Long accountId, Date start, Date end);
}
