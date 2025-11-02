package com.foodordering.service;

import com.foodordering.dto.request.RoleRequest;
import com.foodordering.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    
    RoleResponse create(RoleRequest request);
    
    RoleResponse getById(String id);
    
    RoleResponse getByName(String name);
    
    List<RoleResponse> getAll();
    
    RoleResponse update(String id, RoleRequest request);
    
    void delete(String id);
    
    void softDelete(String id);
}

