package com.foodordering.model.entity;

import com.foodordering.model.abstraction.BaseEntity;
import com.foodordering.model.abstraction.IAuditable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "categories")
public class Category extends BaseEntity implements IAuditable {

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("parent_id")
    private String parentId;

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
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

    @Override
    public Instant getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public Instant getModifiedAt() {
        return this.modifiedAt;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parentId='" + parentId + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", modifiedAt=" + getModifiedAt() +
                '}';
    }
}