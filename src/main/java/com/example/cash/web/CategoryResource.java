package com.example.cash.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cash.dto.CategoryDTO;
import com.example.cash.service.CategoryService;


@RestController
@RequestMapping("/api")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<List<CategoryDTO>> getAllCategory() {
        List<CategoryDTO> dtos = categoryService.findAllCategory();
        return ResponseEntity.ok(dtos);
    }
    
}
