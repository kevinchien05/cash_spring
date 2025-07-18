package com.example.cash.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cash.domain.Setting;
import com.example.cash.domain.User;
import com.example.cash.dto.SettingDTO;
import com.example.cash.exception.ResourceNotFoundException;
import com.example.cash.repository.SettingRepository;
import com.example.cash.repository.UserRepository;
import com.example.cash.service.SettingService;

@Service
public class SettingServiceImpl implements SettingService{

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String addSetting(Long userId,SettingDTO dto) {
        Setting setting = new Setting();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No User Found"));
        setting.setDark(dto.getDark());
        setting.setUser(user);
        settingRepository.save(setting);
        return "Setting created";
    }

    @Override
    public String editSetting(Long userId, SettingDTO dto) {
        Setting setting = settingRepository.findOneByUser_Id(userId);
        setting.setDark(dto.getDark());
        settingRepository.save(setting);
        return "Setting Updated";
    }

    @Override
    public SettingDTO getSetting(Long userId) {
        Setting setting = settingRepository.findOneByUser_Id(userId);
        SettingDTO dto =  new SettingDTO();
        dto.setDark(setting.getDark());
        dto.setUserId(setting.getUser().getId());
        return dto;
    }

}
