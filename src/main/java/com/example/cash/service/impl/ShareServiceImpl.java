package com.example.cash.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cash.domain.Account;
import com.example.cash.domain.Share;
import com.example.cash.domain.User;
import com.example.cash.dto.ShareAccountDTO;
import com.example.cash.dto.ShareDTO;
import com.example.cash.dto.ShareUserDTO;
import com.example.cash.exception.ResourceNotFoundException;
import com.example.cash.repository.AccountRepository;
import com.example.cash.repository.ShareRepository;
import com.example.cash.repository.UserRepository;
import com.example.cash.service.ShareService;

import jakarta.transaction.Transactional;

@Service
public class ShareServiceImpl implements ShareService {

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public List<ShareAccountDTO> getSharedAccount(Long userId) {
        List<Share> shares = shareRepository.findAllByUser_Id(userId);
        return shares.stream()
                .filter(share -> share.getAccount() != null)
                .map(share -> {
                    Account account = share.getAccount(); // no need for findById()
                    ShareAccountDTO dto = new ShareAccountDTO();
                    dto.setId(account.getId());
                    dto.setName(account.getName());
                    dto.setBalance(account.getBalance());
                    dto.setDescription(account.getDescription());
                    dto.setUsername(account.getUser().getUsername());
                    dto.setAccess(share.getAccess());
                    return dto;
                })
                .toList();
    }

    @Override
    public String createShare(Long accountId, ShareDTO dto) {
        Share share = new Share();
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("account not found"));
        if (account.getUser().getEmail().equalsIgnoreCase(dto.getEmail())) {
            return "Cannot Self Share Account";
        } else {
            User user = userRepository.findByEmail(dto.getEmail().toLowerCase());
            if (user != null) {
                share.setAccount(account);
                share.setUser(user);
                share.setAccess(dto.getAccess());
                shareRepository.save(share);
                return "Share Relationship Created";
            } else {
                return "Email Not Found";
            }
        }
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
    public List<ShareUserDTO> getSharedUser(Long accountId) {
        List<Share> shares = shareRepository.findAllByAccount_Id(accountId);
        return shares.stream().map(share -> {
            User user = share.getUser();
            ShareUserDTO dto = new ShareUserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setImage(user.getImage());
            dto.setAccess(share.getAccess());
            return dto;
        }).toList();
    }

    @Transactional
    @Override
    public String editShare(Long accountId, Long userId, Long access) {
        Share share = shareRepository.findOneByAccount_IdAndUser_Id(accountId, userId);
        share.setAccess(access);
        shareRepository.save(share);
        return "Sharing Relationship Edited";
    }

}
