package com.lps.vitalMagic.sales.application.usecase;

import com.lps.vitalMagic.common.presentation.pagination.PageResult;
import com.lps.vitalMagic.sales.application.query.SearchSaleQuery;
import com.lps.vitalMagic.sales.application.view.SaleView;

public interface SearchSaleUseCase {
    PageResult<SaleView> execute(SearchSaleQuery query);
}
