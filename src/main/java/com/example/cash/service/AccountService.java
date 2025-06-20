package com.example.cash.service;

import java.util.List;

import com.example.cash.dto.AccountDTO;

public interface AccountService {
    public List<AccountDTO> findAccountByUser(Long id, String name);

    public String addAccount(AccountDTO dto);

    public String editAccount(AccountDTO dto, Long id);

    public String deleteAccount(Long id); 
}
