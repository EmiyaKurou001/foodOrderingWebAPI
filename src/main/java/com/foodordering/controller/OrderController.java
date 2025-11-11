package com.foodordering.controller;

import com.foodordering.dto.request.OrderRequest;
import com.foodordering.dto.response.OrderResponse;
import com.foodordering.model.entity.Order;
import com.foodordering.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable String id) {
        OrderResponse response = orderService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll() {
        List<OrderResponse> responses = orderService.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<OrderResponse>> getByAccount(@PathVariable String accountId) {
        List<OrderResponse> responses = orderService.getByAccount(accountId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getByStatus(@PathVariable Order.OrderStatus status) {
        List<OrderResponse> responses = orderService.getByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/account/{accountId}/status/{status}")
    public ResponseEntity<List<OrderResponse>> getByAccountAndStatus(
            @PathVariable String accountId,
            @PathVariable Order.OrderStatus status) {
        List<OrderResponse> responses = orderService.getByAccountAndStatus(accountId, status);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable String id, @Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable String id,
            @RequestParam Order.OrderStatus status) {
        OrderResponse response = orderService.updateStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/soft-delete")
    public ResponseEntity<Void> softDelete(@PathVariable String id) {
        orderService.softDelete(id);
        return ResponseEntity.ok().build();
    }
}

