package com.example.cash.web;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cash.dto.TransactionCategoryDTO;
import com.example.cash.dto.TransactionDTO;
import com.example.cash.dto.TransactionSumDTO;
import com.example.cash.service.TransactionService;

@RestController
@RequestMapping("/api")
public class TransactionResource {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add/transaction/{id}")
    public ResponseEntity<String> addNewTransaction(@RequestBody TransactionDTO dto, @PathVariable Long id) throws URISyntaxException {
        transactionService.addNewTransaction(dto, id);
        return ResponseEntity.created(new URI("/add/transaction")).build();
    }

    @PutMapping("edit/transaction/{id}")
    public ResponseEntity<Void> editTransaction(@PathVariable Long id, @RequestBody TransactionDTO dto) {
        transactionService.editTransaction(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/transaction/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/transaction")
    public ResponseEntity<List<TransactionCategoryDTO>> getAllTransactions(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "search", required = false) String search
    ) {
        Date startDate = null;
        Date endDate = null;
        Date todayDate = new Date(System.currentTimeMillis());
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        if (start != null && !start.isEmpty()) {
            startDate = Date.valueOf(start); // must be in yyyy-MM-dd
        } else {
            String today = ft.format(todayDate);
            startDate = Date.valueOf(today);
        }
        if (end != null && !end.isEmpty()) {
            endDate = Date.valueOf(end);
        } else {
            String today = ft.format(todayDate);
            endDate = Date.valueOf(today);
        }

        List<TransactionCategoryDTO> dtos = transactionService.getAllTransaction(id, startDate, endDate, search);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/transaction/income")
    public ResponseEntity<BigDecimal> getIncome(@RequestParam(value = "id") Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end, @RequestParam(value = "category") Long category) {
        Date startDate = null;
        Date endDate = null;
        Date todayDate = new Date(System.currentTimeMillis());
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        if (start != null && !start.isEmpty()) {
            startDate = Date.valueOf(start); // must be in yyyy-MM-dd
        } else {
            String today = ft.format(todayDate);
            startDate = Date.valueOf(today);
        }
        if (end != null && !end.isEmpty()) {
            endDate = Date.valueOf(end);
        } else {
            String today = ft.format(todayDate);
            endDate = Date.valueOf(today);
        }

        BigDecimal total = transactionService.getAccountBalance(id, startDate, endDate, category);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/transaction/budget")
    public ResponseEntity<List<TransactionSumDTO>> getTransactionSum(@RequestParam(value = "id") Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        Date startDate = null;
        Date endDate = null;
        Date todayDate = new Date(System.currentTimeMillis());
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        if (start != null && !start.isEmpty()) {
            startDate = Date.valueOf(start); // must be in yyyy-MM-dd
        } else {
            String today = ft.format(todayDate);
            startDate = Date.valueOf(today);
        }
        if (end != null && !end.isEmpty()) {
            endDate = Date.valueOf(end);
        } else {
            String today = ft.format(todayDate);
            endDate = Date.valueOf(today);
        }
        List<TransactionSumDTO> transactions = transactionService.getTransactionSumByCategory(id, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

}
