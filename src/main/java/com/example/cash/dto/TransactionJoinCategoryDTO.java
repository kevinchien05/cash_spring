package com.example.cash.dto;

import java.math.BigDecimal;

public interface TransactionJoinCategoryDTO {
    Long getCategoryId();

    String getCategoryName();

    BigDecimal getTotal();
}
