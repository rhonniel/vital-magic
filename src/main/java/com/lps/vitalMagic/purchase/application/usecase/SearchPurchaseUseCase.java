package com.lps.vitalMagic.purchase.application.usecase;

import com.lps.vitalMagic.purchase.application.query.SearchPurchasesQuery;
import com.lps.vitalMagic.purchase.application.view.PurchaseView;
import com.lps.vitalMagic.common.presentation.pagination.PageResult;

public interface SearchPurchaseUseCase {
    PageResult<PurchaseView> execute(SearchPurchasesQuery query);
}
