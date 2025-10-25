package com.foodordering.model.abstraction;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Base MongoDB-mapped superclass for all domain objects.
 * Uses a String id (Mongo ObjectId), a soft-delete flag, and audit timestamps.
 * Note: This class no longer implements IAuditable; if a concrete entity needs
 * to expose the audit contract explicitly, it can implement IAuditable itself.
 *
 * Timestamps are populated by a MongoEntityListener which sets createdAt once
 * and updates modifiedAt on every save using Instant.now().
 */
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("is_deleted")
    private boolean isDeleted = false;

    @SuppressWarnings("unused")
    @Field("created_at")
    protected Instant createdAt;

    @SuppressWarnings("unused")
    @Field("modified_at")
    protected Instant modifiedAt;

    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public void restore() {
        this.isDeleted = false;
    }

    // NOTE: getters/setters for createdAt and modifiedAt were removed intentionally.
    // Audit timestamps remain as protected fields so a MongoEntityListener or
    // concrete entity classes implementing an audit interface can manage/access them.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity other = (BaseEntity) o;
        return id != null && other.id != null && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return (id != null) ? Objects.hash(id) : super.hashCode();
    }
}
