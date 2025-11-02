package com.foodordering.dto.response;

import java.time.Instant;

public class CategoryResponse {
    
    private String id;
    private String name;
    private String description;
    private String parentId;
    private Instant createdAt;
    private Instant modifiedAt;

    public CategoryResponse() {
    }

    public CategoryResponse(String id, String name, String description, String parentId, Instant createdAt, Instant modifiedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentId = parentId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}

