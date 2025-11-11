package com.foodordering.service;

import com.foodordering.dto.response.DashboardResponse;

import java.time.YearMonth;
import java.util.List;

public interface DashboardService {
    
    DashboardResponse getMenuItemOrderStatsByMonth(YearMonth startMonth, YearMonth endMonth);
    
    DashboardResponse getMenuItemOrderStatsByMonth(Integer year, Integer month);
    
    DashboardResponse getAllMenuItemOrderStats();
    
    List<DashboardResponse.MenuItemOrderStats> getTopOrderedMenuItems(Integer limit);
}

