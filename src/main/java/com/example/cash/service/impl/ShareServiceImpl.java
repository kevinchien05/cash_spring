package com.example.cash.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cash.domain.Account;
import com.example.cash.domain.Share;
import com.example.cash.domain.User;
import com.example.cash.dto.AccountDTO;
import com.example.cash.dto.UserCreateDTO;
import com.example.cash.exception.ResourceNotFoundException;
import com.example.cash.repository.AccountRepository;
import com.example.cash.repository.ShareRepository;
import com.example.cash.service.ShareService;

import jakarta.transaction.Transactional;

@Service
public class ShareServiceImpl implements ShareService {

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @Override
    public List<AccountDTO> getSharedAccount(Long userId) {
        List<Share> shares = shareRepository.findAllByUser_Id(userId);
        return shares.stream()
                .map(share -> {
                    Account account = share.getAccount(); // no need for findById()
                    AccountDTO dto = new AccountDTO();
                    dto.setId(account.getId());
                    dto.setName(account.getName());
                    dto.setBalance(account.getBalance());
                    dto.setDescription(account.getDescription());
                    dto.setUserId(account.getUser().getId());
                    return dto;
                })
                .toList();
    }

    @Override
    public String createShare(Long accountId, String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createShare'");
    }

    @Transactional
    @Override
    public String deleteShare(Long accoundId, Long userId) {
        Share dto = shareRepository.findOneByAccount_IdAndUser_Id(accoundId, userId);
        shareRepository.delete(dto);
        return "Sharing Relationship Deleted";
    }

    @Transactional
    @Override
    public List<User> getSharedUser(Long accountId) {
        List<Share> shares = shareRepository.findAllByAccount_Id(accountId);
        return shares.stream().map(share -> {
            User user = share.getUser();
            return user;
        }).toList();
    }

}
