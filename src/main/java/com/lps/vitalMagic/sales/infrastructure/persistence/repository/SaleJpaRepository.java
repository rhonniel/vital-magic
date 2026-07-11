package com.lps.vitalMagic.sales.infrastructure.persistence.repository;

import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SaleJpaRepository extends JpaRepository<SaleEntity,Long>, JpaSpecificationExecutor<SaleEntity> {
}
