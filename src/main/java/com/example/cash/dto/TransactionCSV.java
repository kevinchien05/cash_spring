package com.example.cash.dto;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class TransactionCSV {
    @CsvBindByName(column = "date")
    private String date;

    @CsvBindByName(column = "description")
    private String description;

    @CsvBindByName(column = "status")
    private String status;

    @CsvBindByName(column = "total")
    private String total;

    @CsvBindByName(column = "category")
    private String category;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


}
