package com.lps.vitalMagic.purchase.application.usecase;

import com.lps.vitalMagic.purchase.application.query.SearchPurchasesQuery;
import com.lps.vitalMagic.purchase.application.view.PurchaseView;
import com.lps.vitalMagic.sales.application.pagination.PageResult;

import java.util.List;

public interface SearchPurchaseUseCase {
    PageResult<PurchaseView> execute(SearchPurchasesQuery query);
}
