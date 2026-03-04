package com.lps.vitalMagic.sales.repository;

import com.lps.vitalMagic.sales.model.entity.Sale;
import com.lps.vitalMagic.sales.model.dto.SaleFilter;

import java.util.List;
import java.util.Optional;

public interface SaleRepository {
    Optional<Sale> findById(Long id);
    Sale save(Sale sale);
    List<Sale> search(SaleFilter filter);
}
