package com.foodordering.service;

import com.foodordering.dto.request.CategoryRequest;
import com.foodordering.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    
    CategoryResponse create(CategoryRequest request);
    
    CategoryResponse getById(String id);
    
    List<CategoryResponse> getAll();
    
    List<CategoryResponse> getRootCategories();
    
    List<CategoryResponse> getCategoriesByParent(String parentId);
    
    CategoryResponse update(String id, CategoryRequest request);
    
    void delete(String id);
    
    void softDelete(String id);
}

