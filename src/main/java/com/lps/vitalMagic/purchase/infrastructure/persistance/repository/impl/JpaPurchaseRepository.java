package com.lps.vitalMagic.purchase.infrastructure.persistance.repository.impl;

import com.lps.vitalMagic.purchase.application.query.SearchPurchasesQuery;
import com.lps.vitalMagic.purchase.application.view.PurchaseView;
import com.lps.vitalMagic.purchase.domain.model.entity.Purchase;
import com.lps.vitalMagic.purchase.domain.repository.PurchaseRepository;
import com.lps.vitalMagic.purchase.infrastructure.persistance.entity.PurchaseEntity;
import com.lps.vitalMagic.purchase.infrastructure.persistance.mapper.PurchaseMapper;
import com.lps.vitalMagic.purchase.infrastructure.persistance.repository.PurchaseJpaRepository;
import com.lps.vitalMagic.purchase.infrastructure.persistance.specification.PurchaseSpecifications;
import com.lps.vitalMagic.sales.application.pagination.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaPurchaseRepository implements PurchaseRepository {

    private final PurchaseJpaRepository jpaRepository;

    public JpaPurchaseRepository(PurchaseJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Purchase> findById(Long id) {
        return jpaRepository.findById(id).map(PurchaseMapper::toDomain);
    }

    @Override
    public Purchase save(Purchase purchase) {
        PurchaseEntity entity = PurchaseMapper.toEntity(purchase);

        return PurchaseMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public PageResult<PurchaseView> search(SearchPurchasesQuery query) {
        Pageable pageable = PageRequest.of(
                query.pagination().page(),
                query.pagination().size(),
                Sort.by("createdAt").descending()
        );

        Page<PurchaseEntity> page = jpaRepository.findAll(
                PurchaseSpecifications.withFilters(query),
                pageable
        );

        return PurchaseMapper.toPageResult(page);
    }
}
