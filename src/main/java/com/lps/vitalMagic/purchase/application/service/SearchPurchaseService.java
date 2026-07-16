package com.lps.vitalMagic.purchase.application.service;

import com.lps.vitalMagic.purchase.application.query.SearchPurchasesQuery;
import com.lps.vitalMagic.purchase.application.usecase.SearchPurchaseUseCase;
import com.lps.vitalMagic.purchase.application.view.PurchaseView;
import com.lps.vitalMagic.purchase.domain.repository.PurchaseRepository;
import com.lps.vitalMagic.sales.application.pagination.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchPurchaseService  implements SearchPurchaseUseCase {

    public final PurchaseRepository purchaseRepository;

    public SearchPurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public PageResult<PurchaseView> execute(SearchPurchasesQuery query) {
        if (query.from() == null || query.to() == null) {
            throw new IllegalArgumentException("From and To dates are required.");
        }

        if (query.from().isAfter(query.to())) {
            throw new IllegalArgumentException("From should be before To.");
        }

        return purchaseRepository.search(query);
    }
}
