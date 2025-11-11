package com.foodordering.service;

import com.foodordering.dto.request.OrderRequest;
import com.foodordering.dto.response.OrderResponse;
import com.foodordering.model.entity.Order;

import java.util.List;

public interface OrderService {
    
    OrderResponse create(OrderRequest request);
    
    OrderResponse getById(String id);
    
    List<OrderResponse> getAll();
    
    List<OrderResponse> getByAccount(String accountId);
    
    List<OrderResponse> getByStatus(Order.OrderStatus status);
    
    List<OrderResponse> getByAccountAndStatus(String accountId, Order.OrderStatus status);
    
    OrderResponse update(String id, OrderRequest request);
    
    OrderResponse updateStatus(String id, Order.OrderStatus status);
    
    void delete(String id);
    
    void softDelete(String id);
}

