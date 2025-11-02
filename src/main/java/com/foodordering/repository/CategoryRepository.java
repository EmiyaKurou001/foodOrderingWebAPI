package com.foodordering.repository;

import com.foodordering.model.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    
    Optional<Category> findByName(String name);
    
    List<Category> findByParentIdIsNull();
    
    List<Category> findByParentId(String parentId);
    
    boolean existsByName(String name);
}

