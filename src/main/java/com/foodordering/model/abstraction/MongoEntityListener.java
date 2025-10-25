package com.foodordering.model.abstraction;

import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import java.time.Instant;

/**
 * MongoDB event listener that populates audit timestamps on BaseEntity instances.
 * - sets createdAt once (when null)
 * - updates modifiedAt on every save
 */
@Component
public class MongoEntityListener extends AbstractMongoEventListener<BaseEntity> {

    @Override
    public void onBeforeConvert(BeforeConvertEvent<BaseEntity> event) {
        BaseEntity entity = event.getSource();
        if (entity == null) return;

        Instant now = Instant.now();
        if (entity.createdAt == null) {
            entity.createdAt = now;
        }
        entity.modifiedAt = now;
    }
}
