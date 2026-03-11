package com.lps.vitalMagic.purchase.domain.repository;


import com.lps.vitalMagic.purchase.domain.model.entity.Purchase;


import java.util.Optional;

public interface PurchaseRepository {
    Optional<Purchase> findById(Long id);
    Purchase save(Purchase purchase);
    //List<Purchase> search(SearchPurchasesQuery filter); TODO validar Criteria para no vincular entidad Query directo al repository
}
