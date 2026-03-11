package com.lps.vitalMagic.purchase.application.service;

import com.lps.vitalMagic.purchase.application.query.SearchPurchasesQuery;
import com.lps.vitalMagic.purchase.application.usecase.SearchPurchaseUseCase;
import com.lps.vitalMagic.purchase.application.view.PurchaseView;

import java.util.List;

public class SearchPurchaseService  implements SearchPurchaseUseCase {
    @Override
    public List<PurchaseView> execute(SearchPurchasesQuery query) {
        return List.of();
    }
}
