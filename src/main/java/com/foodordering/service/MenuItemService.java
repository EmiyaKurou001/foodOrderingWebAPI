package com.foodordering.service;

import com.foodordering.dto.request.MenuItemRequest;
import com.foodordering.dto.response.MenuItemResponse;

import java.util.List;

public interface MenuItemService {
    
    MenuItemResponse create(MenuItemRequest request);
    
    MenuItemResponse getById(String id);
    
    List<MenuItemResponse> getAll();
    
    List<MenuItemResponse> getAvailableItems();
    
    List<MenuItemResponse> getByCategory(String categoryId);
    
    List<MenuItemResponse> getByCategoryAndAvailable(String categoryId);
    
    List<MenuItemResponse> searchByName(String name);
    
    MenuItemResponse update(String id, MenuItemRequest request);
    
    void delete(String id);
    
    void softDelete(String id);
}

