package com.foodordering.model.entity;

import com.foodordering.model.abstraction.BaseEntity;
import com.foodordering.model.abstraction.IAuditable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.Instant;

/**
 * Example MongoDB document that extends the base entity.
 */
@Document(collection = "products")
public class Product extends BaseEntity implements IAuditable {

    @Field("name")
    private String name;

    @Field("price")
    private Double price;

    public Product() {}

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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
        return "Product{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", isDeleted=" + isDeleted() +
                ", createdAt=" + getCreatedAt() +
                ", modifiedAt=" + getModifiedAt() +
                '}';
    }
}
