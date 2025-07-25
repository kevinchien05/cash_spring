package com.example.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cash.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
