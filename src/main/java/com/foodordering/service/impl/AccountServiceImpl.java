package com.foodordering.service.impl;

import com.foodordering.dto.request.AccountRequest;
import com.foodordering.dto.response.AccountResponse;
import com.foodordering.model.entity.Account;
import com.foodordering.repository.AccountRepository;
import com.foodordering.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public AccountResponse create(AccountRequest request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setEmail(request.getEmail());
        account.setPassword(request.getPassword());
        if (request.getRoles() != null) {
            account.setRoles(request.getRoles());
        }
        if (request.getActive() != null) {
            account.setActive(request.getActive());
        }

        Account saved = accountRepository.save(account);
        return toResponse(saved);
    }

    @Override
    public AccountResponse getById(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        return toResponse(account);
    }

    @Override
    public AccountResponse getByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found with username: " + username));
        return toResponse(account);
    }

    @Override
    public AccountResponse getByEmail(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found with email: " + email));
        return toResponse(account);
    }

    @Override
    public List<AccountResponse> getAll() {
        return accountRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponse> getActiveAccounts() {
        return accountRepository.findByActiveTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse update(String id, AccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        if (request.getUsername() != null && !request.getUsername().equals(account.getUsername())) {
            if (accountRepository.existsByUsername(request.getUsername())) {
                throw new RuntimeException("Username already exists: " + request.getUsername());
            }
            account.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(account.getEmail())) {
            if (accountRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists: " + request.getEmail());
            }
            account.setEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            account.setPassword(request.getPassword());
        }

        if (request.getRoles() != null) {
            account.setRoles(request.getRoles());
        }

        if (request.getActive() != null) {
            account.setActive(request.getActive());
        }

        Account updated = accountRepository.save(account);
        return toResponse(updated);
    }

    @Override
    public void delete(String id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }

    @Override
    public void softDelete(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        account.softDelete();
        accountRepository.save(account);
    }

    @Override
    public AccountResponse restore(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        account.restore();
        Account restored = accountRepository.save(account);
        return toResponse(restored);
    }

    private AccountResponse toResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setUsername(account.getUsername());
        response.setEmail(account.getEmail());
        response.setRoles(account.getRoles());
        response.setActive(account.isActive());
        response.setCreatedAt(account.getCreatedAt());
        response.setModifiedAt(account.getModifiedAt());
        return response;
    }
}

