package com.lps.vitalMagic.purchase.domain.repository;

import com.lps.vitalMagic.purchase.domain.model.entity.Purchase;
import com.lps.vitalMagic.purchase.domain.model.dto.PurchaseFilter;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository {
    Optional<Purchase> findById(Long id);
    Purchase save(Purchase purchase);
    List<Purchase> search(PurchaseFilter filter);
}
