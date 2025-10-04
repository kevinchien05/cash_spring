package com.example.cash.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.cash.dto.BudgetCategoryDTO;
import com.example.cash.dto.BudgetDTO;
import com.example.cash.service.BudgetService;

@RestController
@RequestMapping("/api")
public class BudgetResource {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/add/budget/{id}")
    public ResponseEntity<String> addNewBudget(@RequestBody BudgetDTO dto, @PathVariable("id") Long id) throws URISyntaxException {
        BudgetCategoryDTO result = budgetService.addNewBudget(id, dto);
        if (result != null) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", "ADD");
            payload.put("data", result);
            messagingTemplate.convertAndSend("/topic/budget/" + id, payload);
        }
        return ResponseEntity.created(new URI("/add/budget")).build();
    }

    @GetMapping("/budget")
    public ResponseEntity<List<BudgetCategoryDTO>> getMethodName(@RequestParam(value = "id") Long id, @RequestParam(value = "start", required = false) String start, @RequestParam(value = "end", required = false) String end) {
        Date startDate = Date.valueOf(start); // must be in yyyy-MM-dd
        Date endDate = Date.valueOf(end);
        List<BudgetCategoryDTO> dtos = budgetService.getAllBudget(id, startDate, endDate);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/edit/budget/{id}")
    public ResponseEntity<String> editBudget(@PathVariable Long id, @RequestBody BudgetDTO dto) {
        BudgetCategoryDTO result = budgetService.editBudget(id, dto);
        if(result != null){
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", "EDIT");
            payload.put("data", result);
            payload.put("account", id);
            messagingTemplate.convertAndSend("/topic/budget", payload);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/budget/{id}")
    public ResponseEntity<String> deleteBudget(@PathVariable Long id) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", "DELETE");
        payload.put("data", id);
        messagingTemplate.convertAndSend("/topic/budget", payload);
        budgetService.deleteBudget(id);
        return ResponseEntity.ok().build();
    }

}
