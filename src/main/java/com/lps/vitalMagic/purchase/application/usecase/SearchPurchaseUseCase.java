package com.lps.vitalMagic.purchase.application.usecase;

import com.lps.vitalMagic.purchase.application.query.SearchPurchasesQuery;
import com.lps.vitalMagic.purchase.application.view.PurchaseView;

import java.util.List;

public interface SearchPurchaseUseCase {
    List<PurchaseView> execute(SearchPurchasesQuery query);
}
