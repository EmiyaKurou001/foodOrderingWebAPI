package com.foodordering.controller;

import com.foodordering.dto.request.MenuItemRequest;
import com.foodordering.dto.response.MenuItemResponse;
import com.foodordering.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<MenuItemResponse> create(@Valid @RequestBody MenuItemRequest request) {
        MenuItemResponse response = menuItemService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getById(@PathVariable String id) {
        MenuItemResponse response = menuItemService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> getAll() {
        List<MenuItemResponse> responses = menuItemService.getAll();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/available")
    public ResponseEntity<List<MenuItemResponse>> getAvailableItems() {
        List<MenuItemResponse> responses = menuItemService.getAvailableItems();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MenuItemResponse>> getByCategory(@PathVariable String categoryId) {
        List<MenuItemResponse> responses = menuItemService.getByCategory(categoryId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{categoryId}/available")
    public ResponseEntity<List<MenuItemResponse>> getByCategoryAndAvailable(@PathVariable String categoryId) {
        List<MenuItemResponse> responses = menuItemService.getByCategoryAndAvailable(categoryId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MenuItemResponse>> searchByName(@RequestParam String name) {
        List<MenuItemResponse> responses = menuItemService.searchByName(name);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> update(@PathVariable String id, @Valid @RequestBody MenuItemRequest request) {
        MenuItemResponse response = menuItemService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        menuItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/soft-delete")
    public ResponseEntity<Void> softDelete(@PathVariable String id) {
        menuItemService.softDelete(id);
        return ResponseEntity.ok().build();
    }
}

