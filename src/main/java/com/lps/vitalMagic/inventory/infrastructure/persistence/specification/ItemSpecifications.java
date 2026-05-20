package com.lps.vitalMagic.inventory.infrastructure.persistence.specification;

import com.lps.vitalMagic.inventory.application.query.SearchItemsQuery;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecifications {

    public static Specification<ItemEntity> withFilters(SearchItemsQuery query) {

        Specification<ItemEntity> spec = Specification.allOf();

        if (query.name() != null && !query.name().isBlank()) {
            spec = spec.and(nameContains(query.name()));
        }

        spec.and(isActive());
        return spec;
    }


    private static Specification<ItemEntity> nameContains(String name) {

        return (root, q, cb) ->
                cb.like(
                        cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    private static Specification<ItemEntity> isActive() {
        return (root, q, cb) ->
                cb.isTrue(root.get("active"));
    }
}
