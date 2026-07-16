package com.lps.vitalMagic.purchase.domain.repository;


import com.lps.vitalMagic.purchase.application.query.SearchPurchasesQuery;
import com.lps.vitalMagic.purchase.application.view.PurchaseView;
import com.lps.vitalMagic.purchase.domain.model.entity.Purchase;
import com.lps.vitalMagic.sales.application.pagination.PageResult;


import java.util.Optional;

public interface PurchaseRepository {
    Optional<Purchase> findById(Long id);
    Purchase save(Purchase purchase);
    PageResult<PurchaseView> search(SearchPurchasesQuery filter);
}
