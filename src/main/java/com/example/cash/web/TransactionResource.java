package com.example.cash.web;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.cash.dto.TransactionCategoryDTO;
import com.example.cash.dto.TransactionDTO;
import com.example.cash.dto.TransactionDateDTO;
import com.example.cash.dto.TransactionJoinCategoryDTO;
import com.example.cash.dto.TransactionSumDTO;
import com.example.cash.service.TransactionService;

@RestController
@RequestMapping("/api")
public class TransactionResource {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/add/transaction/{id}")
    public ResponseEntity<String> addNewTransaction(@RequestBody TransactionDTO dto, @PathVariable Long id) throws URISyntaxException {
        TransactionCategoryDTO result = transactionService.addNewTransaction(dto, id);
        if (result != null) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", "ADD");
            payload.put("data", result);
            payload.put("id", id);
            messagingTemplate.convertAndSend("/topic/transaction/" + id, payload);
            return ResponseEntity.created(new URI("/add/transaction")).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("fail");
        }
    }

    @PutMapping("edit/transaction/{id}")
    public ResponseEntity<?> editTransaction(@PathVariable Long id, @RequestBody TransactionDTO dto) {
        TransactionCategoryDTO result = transactionService.editTransaction(id, dto);
        if (result != null) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", "EDIT");
            payload.put("data", result);
            messagingTemplate.convertAndSend("/topic/transaction", payload);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("fail");
        }
    }

    @DeleteMapping("delete/transaction/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", "DELETE");
        payload.put("data", id);
        messagingTemplate.convertAndSend("/topic/transaction", payload);
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

    @GetMapping("/transaction/outcome")
    public ResponseEntity<BigDecimal> getAccountOutcome(@RequestParam(value = "id") Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
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

        BigDecimal total = transactionService.getAccountOutcome(id, startDate, endDate);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/transaction/income/dashboard")
    public ResponseEntity<BigDecimal> getAccountIncomeDashboard(@RequestParam(value = "id") Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
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

        BigDecimal total = transactionService.getAccountIncomeDashboard(id, startDate, endDate);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/transaction/income/date")
    public ResponseEntity<List<TransactionDateDTO>> getIncomeGroupByDate(@RequestParam(value = "id") Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
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

        List<TransactionDateDTO> dtos = transactionService.getTransactionGroupByDate(id, startDate, endDate, true);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/transaction/outcome/date")
    public ResponseEntity<List<TransactionDateDTO>> getOutcomeGroupByDate(@RequestParam(value = "id") Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
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

        List<TransactionDateDTO> dtos = transactionService.getTransactionGroupByDate(id, startDate, endDate, false);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/transaction/outcome/group")
    public ResponseEntity<List<TransactionJoinCategoryDTO>> getTransactionGroupByCategory(@RequestParam(value = "id") Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
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

        List<TransactionJoinCategoryDTO> dtos = transactionService.getTransactionCategoryName(id, startDate, endDate);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/transaction/outcome/limit")
    public ResponseEntity<List<TransactionJoinCategoryDTO>> getTransactionGroupByCategoryLimitOne(@RequestParam(value = "id") Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
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

        List<TransactionJoinCategoryDTO> dtos = transactionService.getTransactionCategoryNameLimitOne(id, startDate, endDate);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping(value = "/transaction/csv/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importTransactionCSV(@RequestParam MultipartFile file, @PathVariable Long id, @RequestParam Boolean count) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid CSV file.");
        }

        try {
            transactionService.importTransactionCSV(file, id, count);
            return ResponseEntity.ok("CSV imported successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

}
