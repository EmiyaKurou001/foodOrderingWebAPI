package com.foodordering.service;

import com.foodordering.dto.request.AccountRequest;
import com.foodordering.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
    
    AccountResponse create(AccountRequest request);
    
    AccountResponse getById(String id);
    
    AccountResponse getByUsername(String username);
    
    AccountResponse getByEmail(String email);
    
    List<AccountResponse> getAll();
    
    List<AccountResponse> getActiveAccounts();
    
    AccountResponse update(String id, AccountRequest request);
    
    void delete(String id);
    
    void softDelete(String id);
    
    AccountResponse restore(String id);
}

