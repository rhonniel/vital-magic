package com.lps.vitalMagic.sales.infrastructure.persistence.repository.impl;

import com.lps.vitalMagic.sales.application.pagination.PageResult;
import com.lps.vitalMagic.sales.application.query.SearchSaleQuery;
import com.lps.vitalMagic.sales.application.view.SaleView;
import com.lps.vitalMagic.sales.domain.model.entity.Sale;
import com.lps.vitalMagic.sales.domain.repository.SaleRepository;
import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleEntity;
import com.lps.vitalMagic.sales.infrastructure.persistence.mapper.SaleMapper;
import com.lps.vitalMagic.sales.infrastructure.persistence.repository.SaleJpaRepository;
import com.lps.vitalMagic.sales.infrastructure.persistence.specification.SaleSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Override
    public PageResult<SaleView> search(SearchSaleQuery query) {
        Pageable pageable = PageRequest.of(
                query.pagination().page(),
                query.pagination().size(),
                Sort.by("createdAt").descending()
        );

        Page<SaleEntity> page = jpaRepository.findAll(
                SaleSpecifications.withFilters(query),
                pageable
        );

        return SaleMapper.toPageResult(page);
    }
}
