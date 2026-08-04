package com.lps.vitalMagic.sales.domain.repository;

import com.lps.vitalMagic.common.presentation.pagination.PageResult;
import com.lps.vitalMagic.sales.application.query.SearchSaleQuery;
import com.lps.vitalMagic.sales.application.view.SaleView;
import com.lps.vitalMagic.sales.domain.model.entity.Sale;

import java.util.Optional;

public interface SaleRepository {
    Optional<Sale> findById(Long id);
    Sale save(Sale sale);
    PageResult<SaleView> search(SearchSaleQuery query);
}
