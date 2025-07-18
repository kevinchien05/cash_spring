package com.example.cash.service;

import com.example.cash.dto.SettingDTO;

public interface SettingService {

    public String addSetting(Long userId, SettingDTO dto);

    public String editSetting(Long userId, SettingDTO dto);

    public SettingDTO getSetting(Long userId);
}
