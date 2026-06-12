package com.lps.vitalMagic.shake.infrastructure.persistance.specification;


import com.lps.vitalMagic.shake.application.query.SearchShakeQuery;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;
import com.lps.vitalMagic.shake.infrastructure.persistance.entity.ShakeEntity;
import org.springframework.data.jpa.domain.Specification;

public class ShakeSpecifications {
    public static Specification<ShakeEntity> withFilters(SearchShakeQuery query) {
        Specification<ShakeEntity> spec = Specification.allOf();
        if (query.category() != null) {
            spec = spec.and(categoryContains(query.category()));
        }

        if (query.type() != null) {
            spec = spec.and(shakeTypeContains(query.type()));
        }
        spec.and(isActive());
        return spec;
    }


    private static Specification<ShakeEntity> categoryContains(ShakeCategory category) {
        return (root, q, cb) ->
                        cb.equal(root.get("category"),category);
    }


    private static Specification<ShakeEntity> shakeTypeContains(ShakeType type) {
        return (root, q, cb) ->
                cb.equal(root.get("category"),type);
    }

    private static Specification<ShakeEntity> isActive() {
        return (root, q, cb) ->
                cb.isTrue(root.get("active"));
    }
}
