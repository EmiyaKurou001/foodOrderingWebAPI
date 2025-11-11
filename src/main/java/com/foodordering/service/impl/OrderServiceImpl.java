package com.foodordering.service.impl;

import com.foodordering.dto.request.OrderRequest;
import com.foodordering.dto.response.OrderResponse;
import com.foodordering.model.entity.MenuItem;
import com.foodordering.model.entity.Order;
import com.foodordering.repository.AccountRepository;
import com.foodordering.repository.MenuItemRepository;
import com.foodordering.repository.OrderRepository;
import com.foodordering.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public OrderResponse create(OrderRequest request) {
        // Validate account exists
        if (!accountRepository.existsById(request.getAccountId())) {
            throw new RuntimeException("Account not found with id: " + request.getAccountId());
        }

        Order order = new Order();
        order.setAccountId(request.getAccountId());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setNotes(request.getNotes());
        order.setStatus(Order.OrderStatus.PENDING);

        // Convert order items and calculate total
        double totalAmount = 0.0;
        List<Order.OrderItem> orderItems = request.getOrderItems().stream()
                .map(itemRequest -> {
                    // Validate menu item exists and get price
                    MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                            .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + itemRequest.getMenuItemId()));

                    if (!menuItem.isAvailable()) {
                        throw new RuntimeException("Menu item is not available: " + menuItem.getName());
                    }

                    Order.OrderItem orderItem = new Order.OrderItem(
                            itemRequest.getMenuItemId(),
                            itemRequest.getQuantity(),
                            menuItem.getPrice()
                    );
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        // Calculate total amount
        totalAmount = orderItems.stream()
                .mapToDouble(Order.OrderItem::getSubtotal)
                .sum();
        order.setTotalAmount(totalAmount);

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    @Override
    public OrderResponse getById(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return toResponse(order);
    }

    @Override
    public List<OrderResponse> getAll() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getByAccount(String accountId) {
        return orderRepository.findByAccountId(accountId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getByAccountAndStatus(String accountId, Order.OrderStatus status) {
        return orderRepository.findByAccountIdAndStatus(accountId, status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse update(String id, OrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Only allow updates to pending orders
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new RuntimeException("Cannot update order with status: " + order.getStatus());
        }

        if (request.getDeliveryAddress() != null) {
            order.setDeliveryAddress(request.getDeliveryAddress());
        }

        if (request.getNotes() != null) {
            order.setNotes(request.getNotes());
        }

        // Update order items if provided
        if (request.getOrderItems() != null && !request.getOrderItems().isEmpty()) {
            List<Order.OrderItem> orderItems = request.getOrderItems().stream()
                    .map(itemRequest -> {
                        MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + itemRequest.getMenuItemId()));

                        if (!menuItem.isAvailable()) {
                            throw new RuntimeException("Menu item is not available: " + menuItem.getName());
                        }

                        return new Order.OrderItem(
                                itemRequest.getMenuItemId(),
                                itemRequest.getQuantity(),
                                menuItem.getPrice()
                        );
                    })
                    .collect(Collectors.toList());

            order.setOrderItems(orderItems);

            // Recalculate total
            double totalAmount = orderItems.stream()
                    .mapToDouble(Order.OrderItem::getSubtotal)
                    .sum();
            order.setTotalAmount(totalAmount);
        }

        Order updated = orderRepository.save(order);
        return toResponse(updated);
    }

    @Override
    public OrderResponse updateStatus(String id, Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.setStatus(status);
        Order updated = orderRepository.save(order);
        return toResponse(updated);
    }

    @Override
    public void delete(String id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public void softDelete(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.softDelete();
        orderRepository.save(order);
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setAccountId(order.getAccountId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setDeliveryAddress(order.getDeliveryAddress());
        response.setNotes(order.getNotes());
        response.setCreatedAt(order.getCreatedAt());
        response.setModifiedAt(order.getModifiedAt());

        // Convert order items
        List<OrderResponse.OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(item -> {
                    OrderResponse.OrderItemResponse itemResponse = new OrderResponse.OrderItemResponse();
                    itemResponse.setMenuItemId(item.getMenuItemId());
                    itemResponse.setQuantity(item.getQuantity());
                    itemResponse.setPrice(item.getPrice());
                    itemResponse.setSubtotal(item.getSubtotal());
                    return itemResponse;
                })
                .collect(Collectors.toList());

        response.setOrderItems(orderItemResponses);
        return response;
    }
}

