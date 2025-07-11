package com.example.cash.dto;

import java.math.BigDecimal;
import java.sql.Date;

public interface TransactionDateDTO {

    Date getDate();

    BigDecimal getTotal();
}
