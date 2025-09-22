package com.example.cash.service;

import java.util.List;

import com.example.cash.domain.User;
import com.example.cash.dto.AccountDTO;
import com.example.cash.dto.UserCreateDTO;

public interface ShareService {
    public List<AccountDTO> getSharedAccount(Long userId);

    public String createShare(Long accountId, String email);

    public String deleteShare(Long accoundId, Long userId);

    public List<User> getSharedUser(Long accountId);
}
