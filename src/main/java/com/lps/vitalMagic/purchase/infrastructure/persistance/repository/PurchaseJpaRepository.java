package com.lps.vitalMagic.purchase.infrastructure.persistance.repository;

import com.lps.vitalMagic.purchase.infrastructure.persistance.entity.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PurchaseJpaRepository extends JpaRepository<PurchaseEntity,Long>, JpaSpecificationExecutor<PurchaseEntity> {
}
