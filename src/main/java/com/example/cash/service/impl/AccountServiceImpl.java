package com.example.cash.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cash.domain.Account;
import com.example.cash.domain.User;
import com.example.cash.dto.AccountDTO;
import com.example.cash.exception.ResourceNotFoundException;
import com.example.cash.repository.AccountRepository;
import com.example.cash.repository.UserRepository;
import com.example.cash.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<AccountDTO> findAccountByUser(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String addAccount(AccountDTO dto) {
        Account account = new Account();
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        account.setName(dto.getName());
        account.setBalance(dto.getBalance());
        account.setDescription(dto.getDescription());
        account.setUser(user);
        accountRepository.save(account);
        return "Account created";
    }

    @Override
    public String editAccount(AccountDTO dto, Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account Not Found"));
        account.setName(dto.getName());
        account.setBalance(dto.getBalance());
        account.setDescription(dto.getDescription());
        accountRepository.save(account);
        return "Account edited";
    }

    @Override
    public String deleteAccount(Long id) {
        accountRepository.deleteById(id);
        return "Account deleted";
    }

}
