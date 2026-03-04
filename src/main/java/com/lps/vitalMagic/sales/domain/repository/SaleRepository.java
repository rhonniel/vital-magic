package com.lps.vitalMagic.sales.domain.repository;

import com.lps.vitalMagic.sales.domain.model.entity.Sale;
import com.lps.vitalMagic.sales.domain.model.dto.SaleFilter;

import java.util.List;
import java.util.Optional;

public interface SaleRepository {
    Optional<Sale> findById(Long id);
    Sale save(Sale sale);
    List<Sale> search(SaleFilter filter);
}
