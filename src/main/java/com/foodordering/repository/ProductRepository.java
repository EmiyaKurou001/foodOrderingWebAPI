package com.foodordering.repository;

import com.foodordering.model.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    // Find all non-deleted products using property name
    List<Product> findByIsDeletedFalse();

    // Explicit MongoDB query by stored field name
    @Query("{ 'is_deleted' : false }")
    List<Product> findAllActive();
}
