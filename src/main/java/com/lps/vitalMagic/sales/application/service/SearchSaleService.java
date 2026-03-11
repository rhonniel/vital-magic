package com.lps.vitalMagic.sales.application.service;

import com.lps.vitalMagic.sales.application.query.SearchSaleQuery;
import com.lps.vitalMagic.sales.application.usecase.SearchSaleUseCase;
import com.lps.vitalMagic.sales.application.view.SaleView;

import java.util.List;

public class SearchSaleService implements SearchSaleUseCase
{
    @Override
    public List<SaleView> execute(SearchSaleQuery query) {
        return List.of();
    }
}
