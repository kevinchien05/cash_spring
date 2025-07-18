package com.example.cash.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cash.dto.SettingDTO;
import com.example.cash.service.SettingService;

@RestController
@RequestMapping("/api")
public class SettingResource {

    @Autowired
    private SettingService settingService;

    @PostMapping("/add/setting/{id}")
    public ResponseEntity<Void> addSetting(@PathVariable Long id, @RequestBody SettingDTO dto) {
        settingService.addSetting(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit/setting/{id}")
    public ResponseEntity<Void> editSetting(@PathVariable Long id, @RequestBody SettingDTO dto) {
        settingService.editSetting(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/setting")
    public ResponseEntity<SettingDTO> getMethodName(@RequestParam(value = "id", required = true) Long id) {
        SettingDTO dto = settingService.getSetting(id);
        return ResponseEntity.ok(dto);
    }

}
