package com.example.cash.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cash.domain.Category;
import com.example.cash.dto.CategoryDTO;
import com.example.cash.repository.CategoryRepository;
import com.example.cash.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> findAllCategory() {
        List<Category> categories = categoryRepository.findAllCategoryLast();
        return categories.stream().map((category) -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            return dto;
        }).collect(Collectors.toList());
    }

}
