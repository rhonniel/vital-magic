package com.lps.vitalMagic.sales.infrastructure.persistence.repository.impl;

import com.lps.vitalMagic.sales.domain.model.entity.Sale;
import com.lps.vitalMagic.sales.domain.repository.SaleRepository;
import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleEntity;
import com.lps.vitalMagic.sales.infrastructure.persistence.mapper.SaleMapper;
import com.lps.vitalMagic.sales.infrastructure.persistence.repository.SaleJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class JpaSaleRepository implements SaleRepository {

    private final SaleJpaRepository jpaRepository;

    public JpaSaleRepository(SaleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Sale> findById(Long id) {
        return jpaRepository.findById(id).map(SaleMapper::toDomain);
    }

    @Override
    public Sale save(Sale sale) {
        SaleEntity entity= SaleMapper.toEntity(sale);
        return SaleMapper.toDomain(jpaRepository.save(entity));
    }
}
