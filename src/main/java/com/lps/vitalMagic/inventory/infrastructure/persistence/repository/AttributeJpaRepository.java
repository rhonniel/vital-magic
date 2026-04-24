package com.lps.vitalMagic.inventory.infrastructure.persistence.repository;

import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeJpaRepository extends JpaRepository<AttributeEntity,Long> {
}
