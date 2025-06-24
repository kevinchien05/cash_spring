package com.example.cash.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cash.dto.TransactionCategoryDTO;
import com.example.cash.dto.TransactionDTO;
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

    @GetMapping("/transaction")
    public ResponseEntity<List<TransactionCategoryDTO>> getAllTransactions(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "search", required = false) String search
    ) {
        Date startDate = null;
        Date endDate = null;

        if (start != null && !start.isEmpty()) {
            startDate = Date.valueOf(start); // must be in yyyy-MM-dd
        }
        if (end != null && !end.isEmpty()) {
            endDate = Date.valueOf(end);
        }

        List<TransactionCategoryDTO> dtos = transactionService.getAllTransaction(id, startDate, endDate, search);
        return ResponseEntity.ok(dtos);
    }
}
