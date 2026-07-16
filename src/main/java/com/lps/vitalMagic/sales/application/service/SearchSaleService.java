package com.lps.vitalMagic.sales.application.service;

import com.lps.vitalMagic.sales.application.pagination.PageResult;
import com.lps.vitalMagic.sales.application.query.SearchSaleQuery;
import com.lps.vitalMagic.sales.application.usecase.SearchSaleUseCase;
import com.lps.vitalMagic.sales.application.view.SaleView;
import com.lps.vitalMagic.sales.domain.repository.SaleRepository;
import org.springframework.stereotype.Service;


@Service
public class SearchSaleService implements SearchSaleUseCase {

    private final SaleRepository saleRepository;

    public SearchSaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public PageResult<SaleView> execute(SearchSaleQuery query) {
        if (query.from() == null || query.to() == null) {
            throw new IllegalArgumentException("From and To dates are required.");
        }

        if (query.from().isAfter(query.to())) {
            throw new IllegalArgumentException("From should be before To.");
        }
        return  saleRepository.search(query);
    }
}
