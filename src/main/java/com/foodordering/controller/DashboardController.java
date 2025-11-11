package com.foodordering.controller;

import com.foodordering.dto.response.DashboardResponse;
import com.foodordering.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * Get menu item order statistics for all time
     */
    @GetMapping("/menu-item-stats")
    public ResponseEntity<DashboardResponse> getAllMenuItemStats() {
        DashboardResponse response = dashboardService.getAllMenuItemOrderStats();
        return ResponseEntity.ok(response);
    }

    /**
     * Get menu item order statistics for a specific month
     * @param year Year (e.g., 2024)
     * @param month Month (1-12)
     */
    @GetMapping("/menu-item-stats/month")
    public ResponseEntity<DashboardResponse> getMenuItemStatsByMonth(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        DashboardResponse response = dashboardService.getMenuItemOrderStatsByMonth(year, month);
        return ResponseEntity.ok(response);
    }

    /**
     * Get menu item order statistics for a date range
     * @param startMonth Start month in format "YYYY-MM" (e.g., "2024-01")
     * @param endMonth End month in format "YYYY-MM" (e.g., "2024-12")
     */
    @GetMapping("/menu-item-stats/range")
    public ResponseEntity<DashboardResponse> getMenuItemStatsByRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth startMonth,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth endMonth) {
        DashboardResponse response = dashboardService.getMenuItemOrderStatsByMonth(startMonth, endMonth);
        return ResponseEntity.ok(response);
    }

    /**
     * Get top N most ordered menu items
     * @param limit Number of items to return (default: 10)
     */
    @GetMapping("/top-menu-items")
    public ResponseEntity<List<DashboardResponse.MenuItemOrderStats>> getTopOrderedMenuItems(
            @RequestParam(defaultValue = "10") Integer limit) {
        List<DashboardResponse.MenuItemOrderStats> response = dashboardService.getTopOrderedMenuItems(limit);
        return ResponseEntity.ok(response);
    }
}

