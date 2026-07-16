package com.lps.vitalMagic.purchase.infrastructure.persistance.specification;

import com.lps.vitalMagic.purchase.application.query.SearchPurchasesQuery;
import com.lps.vitalMagic.purchase.infrastructure.persistance.entity.PurchaseEntity;
import com.lps.vitalMagic.purchase.infrastructure.persistance.entity.PurchaseItemEntity;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;

public class PurchaseSpecifications {

    public static Specification<PurchaseEntity> withFilters(SearchPurchasesQuery filters){
        Specification<PurchaseEntity> spec = Specification.allOf();
        if (filters.from() != null) {
            spec = spec.and(dateBetween(filters.from(),filters.to()));
        }

        if (filters.itemId() != null) {
            spec = spec.and(hasItem(filters.itemId()));
        }
        return spec;
    }

    public static Specification<PurchaseEntity> dateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) ->
                cb.between(root.get("createdAt"), from.atStartOfDay(), to.atTime(LocalTime.MAX));
    }

    public static Specification<PurchaseEntity> hasItem(Long itemId){
        return (root, query, cb) -> {

            Subquery<Long> subquery = query.subquery(Long.class);

            Root<PurchaseItemEntity> saleItem = subquery.from(PurchaseItemEntity.class);

            subquery.select(cb.literal(1L))
                    .where(
                            cb.equal(saleItem.get("purchase"), root),
                            cb.equal(saleItem.get("itemId"), itemId)
                    );

            return cb.exists(subquery);
        };
    }
}
