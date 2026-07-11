package com.lps.vitalMagic.sales.domain.repository;

import com.lps.vitalMagic.sales.application.pagination.PageResult;
import com.lps.vitalMagic.sales.application.query.SearchSaleQuery;
import com.lps.vitalMagic.sales.application.view.SaleView;
import com.lps.vitalMagic.sales.domain.model.entity.Sale;
import com.lps.vitalMagic.shake.application.query.SearchShakeQuery;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;

import java.util.List;
import java.util.Optional;

public interface SaleRepository {
    Optional<Sale> findById(Long id);
    Sale save(Sale sale);
    PageResult<SaleView> searchAvailableShakes(SearchSaleQuery query);
}
