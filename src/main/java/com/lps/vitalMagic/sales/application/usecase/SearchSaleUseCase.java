package com.lps.vitalMagic.sales.application.usecase;

import com.lps.vitalMagic.sales.application.query.SearchSaleQuery;
import com.lps.vitalMagic.sales.application.view.SaleView;

import java.util.List;

public interface SearchSaleUseCase {
    List<SaleView> execute(SearchSaleQuery query);
}
