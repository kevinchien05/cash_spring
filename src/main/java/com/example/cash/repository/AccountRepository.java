package com.example.cash.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cash.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUser_Id (Long userID);

    List<Account> findByUser_IdAndNameContainingIgnoreCase(Long userId, String name);

    List<Account> findAllByUser_IdAndNameLikeIgnoreCase(Long userId, String name);

}
