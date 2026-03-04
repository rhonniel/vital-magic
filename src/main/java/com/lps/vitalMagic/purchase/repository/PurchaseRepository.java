package com.lps.vitalMagic.purchase.repository;

import com.lps.vitalMagic.purchase.model.entity.Purchase;
import com.lps.vitalMagic.purchase.model.dto.PurchaseFilter;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository {
    Optional<Purchase> findById(Long id);
    Purchase save(Purchase purchase);
    List<Purchase> search(PurchaseFilter filter);
}
