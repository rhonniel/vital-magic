package com.lps.vitalMagic.sales.infrastructure.persistence.specification;

import com.lps.vitalMagic.sales.application.query.SearchSaleQuery;
import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleEntity;
import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleItemEntity;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;

public class SaleSpecifications {

    public static Specification<SaleEntity> withFilters(SearchSaleQuery filters){
        Specification<SaleEntity> spec = Specification.allOf();
        if (filters.from() != null) {
            spec = spec.and(dateBetween(filters.from(),filters.to()));
        }

        if (filters.productId() != null) {
            spec = spec.and(hasProduct(filters.productId()));
        }
        return spec;
    }

    public static Specification<SaleEntity> dateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) ->
                cb.between(root.get("createdAt"), from.atStartOfDay(), to.atTime(LocalTime.MAX));
    }

    public static Specification<SaleEntity> hasProduct(Long productId){
        return (root, query, cb) -> {

            Subquery<Long> subquery = query.subquery(Long.class);

            Root<SaleItemEntity> saleItem = subquery.from(SaleItemEntity.class);

            subquery.select(cb.literal(1L))
                    .where(
                            cb.equal(saleItem.get("sale"), root),
                            cb.equal(saleItem.get("productId"), productId)
                    );

            return cb.exists(subquery);
        };
    }
}
