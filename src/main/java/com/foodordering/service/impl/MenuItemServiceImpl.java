package com.foodordering.service.impl;

import com.foodordering.dto.request.MenuItemRequest;
import com.foodordering.dto.response.MenuItemResponse;
import com.foodordering.model.entity.MenuItem;
import com.foodordering.repository.MenuItemRepository;
import com.foodordering.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public MenuItemResponse create(MenuItemRequest request) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategoryId(request.getCategoryId());
        if (request.getAvailable() != null) {
            menuItem.setAvailable(request.getAvailable());
        }
        if (request.getImages() != null) {
            menuItem.setImages(request.getImages());
        }
        if (request.getTags() != null) {
            menuItem.setTags(request.getTags());
        }

        MenuItem saved = menuItemRepository.save(menuItem);
        return toResponse(saved);
    }

    @Override
    public MenuItemResponse getById(String id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
        return toResponse(menuItem);
    }

    @Override
    public List<MenuItemResponse> getAll() {
        return menuItemRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemResponse> getAvailableItems() {
        return menuItemRepository.findByAvailableTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemResponse> getByCategory(String categoryId) {
        return menuItemRepository.findByCategoryId(categoryId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemResponse> getByCategoryAndAvailable(String categoryId) {
        return menuItemRepository.findByCategoryIdAndAvailableTrue(categoryId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItemResponse> searchByName(String name) {
        return menuItemRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MenuItemResponse update(String id, MenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));

        if (request.getName() != null) {
            menuItem.setName(request.getName());
        }

        if (request.getDescription() != null) {
            menuItem.setDescription(request.getDescription());
        }

        if (request.getPrice() != null) {
            menuItem.setPrice(request.getPrice());
        }

        if (request.getCategoryId() != null) {
            menuItem.setCategoryId(request.getCategoryId());
        }

        if (request.getAvailable() != null) {
            menuItem.setAvailable(request.getAvailable());
        }

        if (request.getImages() != null) {
            menuItem.setImages(request.getImages());
        }

        if (request.getTags() != null) {
            menuItem.setTags(request.getTags());
        }

        MenuItem updated = menuItemRepository.save(menuItem);
        return toResponse(updated);
    }

    @Override
    public void delete(String id) {
        if (!menuItemRepository.existsById(id)) {
            throw new RuntimeException("Menu item not found with id: " + id);
        }
        menuItemRepository.deleteById(id);
    }

    @Override
    public void softDelete(String id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
        menuItem.softDelete();
        menuItemRepository.save(menuItem);
    }

    private MenuItemResponse toResponse(MenuItem menuItem) {
        MenuItemResponse response = new MenuItemResponse();
        response.setId(menuItem.getId());
        response.setName(menuItem.getName());
        response.setDescription(menuItem.getDescription());
        response.setPrice(menuItem.getPrice());
        response.setCategoryId(menuItem.getCategoryId());
        response.setAvailable(menuItem.isAvailable());
        response.setImages(menuItem.getImages());
        response.setTags(menuItem.getTags());
        response.setCreatedAt(menuItem.getCreatedAt());
        response.setModifiedAt(menuItem.getModifiedAt());
        return response;
    }
}

