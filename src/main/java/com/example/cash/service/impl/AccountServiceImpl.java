package com.example.cash.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.example.cash.domain.Account;
import com.example.cash.domain.User;
import com.example.cash.dto.AccountDTO;
import com.example.cash.exception.ResourceNotFoundException;
import com.example.cash.repository.AccountRepository;
import com.example.cash.repository.UserRepository;
import com.example.cash.service.AccountService;

import jakarta.transaction.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public List<AccountDTO> findAccountByUser(Long id, String name) {
        // User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        // List<Account> accounts = accountRepository.findAllByUser_Id(id);
        name = ObjectUtils.isEmpty(name)?"%":"%"+name+"%";
        List<Account> accounts = accountRepository.findAllByUser_IdAndNameLikeIgnoreCase(id, name);
        System.out.println("Account size: " + accounts.size());
        return accounts.stream().map((account) -> {
            AccountDTO dto = new AccountDTO();
            dto.setId(account.getId());
            dto.setName(account.getName());
            dto.setBalance(account.getBalance());
            dto.setDescription(account.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public String addAccount(AccountDTO dto) {
        Account account = new Account();
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        List<Account> accounts = accountRepository.findAllByUser_Id(user.getId());
        Boolean contains = accounts.stream().anyMatch(acc -> acc.getName().equalsIgnoreCase(dto.getName()));
        if (!contains) {
            account.setName(dto.getName());
            account.setBalance(dto.getBalance());
            account.setDescription(dto.getDescription());
            account.setUser(user);
            accountRepository.save(account);
            return "Account created";
        }
        return "Account is already created";
    }

    @Transactional
    @Override
    public String editAccount(AccountDTO dto, Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account Not Found"));
        if (account.getName().equals(dto.getName())) {
            account.setName(dto.getName());
            account.setBalance(dto.getBalance());
            account.setDescription(dto.getDescription());
            accountRepository.save(account);
            return "Account edited";
        } else {
            User user = userRepository.findById(account.getUser().getId()).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
            List<Account> accounts = accountRepository.findAllByUser_Id(user.getId());
            Boolean contains = accounts.stream().anyMatch(acc -> acc.getName().equalsIgnoreCase(dto.getName()));
            if (!contains) {
                account.setName(dto.getName());
                account.setBalance(dto.getBalance());
                account.setDescription(dto.getDescription());
                account.setUser(user);
                accountRepository.save(account);
                return "Account edited";
            }
            return "Account name is already exists";
        }
    }

    @Override
    public String deleteAccount(Long id) {
        accountRepository.deleteById(id);
        return "Account deleted";
    }

}
