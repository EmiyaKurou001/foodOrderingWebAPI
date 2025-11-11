package com.foodordering.service.impl;

import com.foodordering.dto.response.DashboardResponse;
import com.foodordering.model.entity.MenuItem;
import com.foodordering.model.entity.Order;
import com.foodordering.repository.MenuItemRepository;
import com.foodordering.repository.OrderRepository;
import com.foodordering.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public DashboardResponse getMenuItemOrderStatsByMonth(YearMonth startMonth, YearMonth endMonth) {
        // Get all orders
        List<Order> allOrders = orderRepository.findAll();
        
        // Filter orders by date range and status (exclude cancelled)
        List<Order> filteredOrders = allOrders.stream()
                .filter(order -> {
                    if (order.getCreatedAt() == null) return false;
                    YearMonth orderMonth = YearMonth.from(order.getCreatedAt().atZone(ZoneId.systemDefault()));
                    return !orderMonth.isBefore(startMonth) && !orderMonth.isAfter(endMonth) &&
                           order.getStatus() != Order.OrderStatus.CANCELLED;
                })
                .collect(Collectors.toList());

        return buildDashboardResponse(filteredOrders);
    }

    @Override
    public DashboardResponse getMenuItemOrderStatsByMonth(Integer year, Integer month) {
        YearMonth targetMonth = YearMonth.of(year, month);
        return getMenuItemOrderStatsByMonth(targetMonth, targetMonth);
    }

    @Override
    public DashboardResponse getAllMenuItemOrderStats() {
        List<Order> allOrders = orderRepository.findAll().stream()
                .filter(order -> order.getStatus() != Order.OrderStatus.CANCELLED)
                .collect(Collectors.toList());
        return buildDashboardResponse(allOrders);
    }

    @Override
    public List<DashboardResponse.MenuItemOrderStats> getTopOrderedMenuItems(Integer limit) {
        DashboardResponse dashboard = getAllMenuItemOrderStats();
        List<DashboardResponse.MenuItemOrderStats> stats = dashboard.getMenuItemStats();
        
        return stats.stream()
                .sorted((a, b) -> Integer.compare(
                        b.getTotalOrders() != null ? b.getTotalOrders() : 0,
                        a.getTotalOrders() != null ? a.getTotalOrders() : 0
                ))
                .limit(limit != null ? limit : 10)
                .collect(Collectors.toList());
    }

    private DashboardResponse buildDashboardResponse(List<Order> orders) {
        DashboardResponse response = new DashboardResponse();
        
        // Map to store statistics: menuItemId -> month -> count
        Map<String, Map<String, Integer>> menuItemMonthCount = new HashMap<>();
        Map<String, Map<String, Integer>> menuItemMonthQuantity = new HashMap<>();
        Map<String, Map<String, Double>> menuItemMonthRevenue = new HashMap<>();
        
        // Map to store menu item names
        Map<String, String> menuItemNames = new HashMap<>();
        
        // Process each order
        for (Order order : orders) {
            if (order.getCreatedAt() == null) continue;
            
            YearMonth orderMonth = YearMonth.from(order.getCreatedAt().atZone(ZoneId.systemDefault()));
            String monthKey = orderMonth.toString(); // Format: "YYYY-MM"
            
            // Process each order item
            for (Order.OrderItem orderItem : order.getOrderItems()) {
                String menuItemId = orderItem.getMenuItemId();
                
                // Initialize maps if needed
                menuItemMonthCount.putIfAbsent(menuItemId, new HashMap<>());
                menuItemMonthQuantity.putIfAbsent(menuItemId, new HashMap<>());
                menuItemMonthRevenue.putIfAbsent(menuItemId, new HashMap<>());
                
                // Update counts
                menuItemMonthCount.get(menuItemId).merge(monthKey, 1, Integer::sum);
                menuItemMonthQuantity.get(menuItemId).merge(monthKey, 
                        orderItem.getQuantity() != null ? orderItem.getQuantity() : 0, Integer::sum);
                menuItemMonthRevenue.get(menuItemId).merge(monthKey,
                        orderItem.getSubtotal() != null ? orderItem.getSubtotal() : 0.0, Double::sum);
                
                // Store menu item name if not already stored
                if (!menuItemNames.containsKey(menuItemId)) {
                    MenuItem menuItem = menuItemRepository.findById(menuItemId).orElse(null);
                    if (menuItem != null) {
                        menuItemNames.put(menuItemId, menuItem.getName());
                    } else {
                        menuItemNames.put(menuItemId, "Unknown Item");
                    }
                }
            }
        }
        
        // Build MenuItemOrderStats list
        List<DashboardResponse.MenuItemOrderStats> menuItemStats = new ArrayList<>();
        
        for (String menuItemId : menuItemMonthCount.keySet()) {
            DashboardResponse.MenuItemOrderStats stats = new DashboardResponse.MenuItemOrderStats();
            stats.setMenuItemId(menuItemId);
            stats.setMenuItemName(menuItemNames.getOrDefault(menuItemId, "Unknown Item"));
            
            // Build orders by month map
            Map<String, Integer> ordersByMonth = new HashMap<>();
            Map<String, Integer> quantityByMonth = menuItemMonthQuantity.get(menuItemId);
            Map<String, Double> revenueByMonth = menuItemMonthRevenue.get(menuItemId);
            
            // Combine all months from all maps
            Set<String> allMonths = new HashSet<>();
            allMonths.addAll(menuItemMonthCount.get(menuItemId).keySet());
            allMonths.addAll(quantityByMonth.keySet());
            allMonths.addAll(revenueByMonth.keySet());
            
            int totalOrders = 0;
            int totalQuantity = 0;
            double totalRevenue = 0.0;
            
            for (String month : allMonths) {
                int orderCount = menuItemMonthCount.get(menuItemId).getOrDefault(month, 0);
                ordersByMonth.put(month, orderCount);
                totalOrders += orderCount;
                totalQuantity += quantityByMonth.getOrDefault(month, 0);
                totalRevenue += revenueByMonth.getOrDefault(month, 0.0);
            }
            
            stats.setOrdersByMonth(ordersByMonth);
            stats.setTotalOrders(totalOrders);
            stats.setTotalQuantity(totalQuantity);
            stats.setTotalRevenue(totalRevenue);
            
            menuItemStats.add(stats);
        }
        
        // Sort by total orders descending
        menuItemStats.sort((a, b) -> Integer.compare(
                b.getTotalOrders() != null ? b.getTotalOrders() : 0,
                a.getTotalOrders() != null ? a.getTotalOrders() : 0
        ));
        
        response.setMenuItemStats(menuItemStats);
        
        // Build monthly summary
        Map<String, DashboardResponse.MonthlyStats> monthlySummary = new HashMap<>();
        
        for (Order order : orders) {
            if (order.getCreatedAt() == null) continue;
            
            YearMonth orderMonth = YearMonth.from(order.getCreatedAt().atZone(ZoneId.systemDefault()));
            String monthKey = orderMonth.toString();
            
            monthlySummary.putIfAbsent(monthKey, new DashboardResponse.MonthlyStats(monthKey));
            DashboardResponse.MonthlyStats monthStats = monthlySummary.get(monthKey);
            
            monthStats.setTotalOrders(monthStats.getTotalOrders() != null ? 
                    monthStats.getTotalOrders() + 1 : 1);
            
            int menuItemsInOrder = order.getOrderItems().size();
            monthStats.setTotalMenuItemsOrdered(monthStats.getTotalMenuItemsOrdered() != null ?
                    monthStats.getTotalMenuItemsOrdered() + menuItemsInOrder : menuItemsInOrder);
            
            monthStats.setTotalRevenue(monthStats.getTotalRevenue() != null ?
                    monthStats.getTotalRevenue() + (order.getTotalAmount() != null ? order.getTotalAmount() : 0.0) :
                    (order.getTotalAmount() != null ? order.getTotalAmount() : 0.0));
        }
        
        response.setMonthlySummary(monthlySummary);
        
        // Calculate totals
        response.setTotalOrders(orders.size());
        response.setTotalRevenue(orders.stream()
                .mapToDouble(order -> order.getTotalAmount() != null ? order.getTotalAmount() : 0.0)
                .sum());
        
        return response;
    }
}

