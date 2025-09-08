package com.example.cash.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.cash.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    public static final String SELECT_FROM_CATEGORY_LAST = "SELECT * FROM category ORDER BY (name = 'Others') ASC, id";

    @Query(value = SELECT_FROM_CATEGORY_LAST, nativeQuery = true)
    List<Category> findAllCategoryLast();

}
