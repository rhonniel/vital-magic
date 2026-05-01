package com.lps.vitalMagic.sales.infrastructure.persistence.repository;

import com.lps.vitalMagic.sales.infrastructure.persistence.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleJpaRepository extends JpaRepository<SaleEntity,Long> {
}
