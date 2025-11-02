package com.foodordering.repository;

import com.foodordering.model.entity.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    
    List<MenuItem> findByCategoryId(String categoryId);
    
    List<MenuItem> findByAvailableTrue();
    
    List<MenuItem> findByCategoryIdAndAvailableTrue(String categoryId);
    
    List<MenuItem> findByTagsContaining(String tag);
    
    List<MenuItem> findByNameContainingIgnoreCase(String name);
    
    boolean existsByName(String name);
}

