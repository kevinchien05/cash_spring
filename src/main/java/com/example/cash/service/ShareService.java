package com.example.cash.service;

import java.util.List;

import com.example.cash.dto.ShareAccountDTO;
import com.example.cash.dto.ShareDTO;
import com.example.cash.dto.ShareUserDTO;

public interface ShareService {
    public List<ShareAccountDTO> getSharedAccount(Long userId);

    public String createShare(Long accountId, ShareDTO dto);

    public String deleteShare(Long accoundId, Long userId);

    public List<ShareUserDTO> getSharedUser(Long accountId);

    public String editShare(Long accountId, Long userId, Long access);
}
