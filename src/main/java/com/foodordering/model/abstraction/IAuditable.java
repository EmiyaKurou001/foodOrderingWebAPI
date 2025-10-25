package com.foodordering.model.abstraction;

import java.time.Instant;

/**
 * Simple interface that exposes audit timestamps for domain objects.
 * Implementations should return the creation and last-modified instants.
 */
public interface IAuditable {

    /** Returns the instant when the entity was created (set by auditing). */
    Instant getCreatedAt();

    /** Returns the instant when the entity was last modified (set by auditing). */
    Instant getModifiedAt();

}
