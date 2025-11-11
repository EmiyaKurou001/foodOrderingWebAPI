package com.foodordering.repository;

import com.foodordering.model.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    
    List<Order> findByAccountId(String accountId);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    List<Order> findByAccountIdAndStatus(String accountId, Order.OrderStatus status);
}

