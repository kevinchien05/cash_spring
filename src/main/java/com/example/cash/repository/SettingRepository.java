package com.example.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cash.domain.Setting;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    Setting findOneByUser_Id(Long userId);
}
