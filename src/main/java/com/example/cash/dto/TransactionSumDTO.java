package com.example.cash.dto;

import java.math.BigDecimal;

public interface TransactionSumDTO {

    Long getCategoryId();
    
    BigDecimal getTotal();
}
