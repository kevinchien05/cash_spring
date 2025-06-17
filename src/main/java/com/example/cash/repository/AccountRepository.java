package com.example.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cash.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
