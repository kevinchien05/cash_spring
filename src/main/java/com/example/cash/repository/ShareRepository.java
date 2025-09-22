package com.example.cash.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cash.domain.Share;

public interface ShareRepository extends JpaRepository<Share, Long> {
    List<Share> findAllByUser_Id(Long userId);

    List<Share> findAllByAccount_Id(Long accountId);
}
