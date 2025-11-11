package com.foodordering.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardResponse {
    
    private List<MenuItemOrderStats> menuItemStats;
    private Map<String, MonthlyStats> monthlySummary;
    private Integer totalOrders;
    private Double totalRevenue;

    public DashboardResponse() {
    }

    public List<MenuItemOrderStats> getMenuItemStats() {
        return menuItemStats;
    }

    public void setMenuItemStats(List<MenuItemOrderStats> menuItemStats) {
        this.menuItemStats = menuItemStats;
    }

    public Map<String, MonthlyStats> getMonthlySummary() {
        return monthlySummary;
    }

    public void setMonthlySummary(Map<String, MonthlyStats> monthlySummary) {
        this.monthlySummary = monthlySummary;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    // Nested class for Menu Item Order Statistics
    public static class MenuItemOrderStats {
        private String menuItemId;
        private String menuItemName;
        private Map<String, Integer> ordersByMonth; // Key: "YYYY-MM", Value: order count
        private Integer totalOrders;
        private Integer totalQuantity;
        private Double totalRevenue;

        public MenuItemOrderStats() {
        }

        public String getMenuItemId() {
            return menuItemId;
        }

        public void setMenuItemId(String menuItemId) {
            this.menuItemId = menuItemId;
        }

        public String getMenuItemName() {
            return menuItemName;
        }

        public void setMenuItemName(String menuItemName) {
            this.menuItemName = menuItemName;
        }

        public Map<String, Integer> getOrdersByMonth() {
            return ordersByMonth;
        }

        public void setOrdersByMonth(Map<String, Integer> ordersByMonth) {
            this.ordersByMonth = ordersByMonth;
        }

        public Integer getTotalOrders() {
            return totalOrders;
        }

        public void setTotalOrders(Integer totalOrders) {
            this.totalOrders = totalOrders;
        }

        public Integer getTotalQuantity() {
            return totalQuantity;
        }

        public void setTotalQuantity(Integer totalQuantity) {
            this.totalQuantity = totalQuantity;
        }

        public Double getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(Double totalRevenue) {
            this.totalRevenue = totalRevenue;
        }
    }

    // Nested class for Monthly Statistics
    public static class MonthlyStats {
        private String month; // Format: "YYYY-MM"
        private Integer totalOrders;
        private Integer totalMenuItemsOrdered;
        private Double totalRevenue;

        public MonthlyStats() {
        }

        public MonthlyStats(String month) {
            this.month = month;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public Integer getTotalOrders() {
            return totalOrders;
        }

        public void setTotalOrders(Integer totalOrders) {
            this.totalOrders = totalOrders;
        }

        public Integer getTotalMenuItemsOrdered() {
            return totalMenuItemsOrdered;
        }

        public void setTotalMenuItemsOrdered(Integer totalMenuItemsOrdered) {
            this.totalMenuItemsOrdered = totalMenuItemsOrdered;
        }

        public Double getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(Double totalRevenue) {
            this.totalRevenue = totalRevenue;
        }
    }
}

