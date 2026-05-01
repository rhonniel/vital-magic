package com.lps.vitalMagic.purchase.infrastructure.persistance.repository.impl;

import com.lps.vitalMagic.purchase.domain.model.entity.Purchase;
import com.lps.vitalMagic.purchase.domain.repository.PurchaseRepository;
import com.lps.vitalMagic.purchase.infrastructure.persistance.entity.PurchaseEntity;
import com.lps.vitalMagic.purchase.infrastructure.persistance.mapper.PurchaseMapper;
import com.lps.vitalMagic.purchase.infrastructure.persistance.repository.PurchaseJpaRepository;
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
}
