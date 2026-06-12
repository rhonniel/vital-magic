package com.lps.vitalMagic.shake.infrastructure.persistance.repository;

import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.shake.infrastructure.persistance.entity.ShakeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShakeEntityJpaRepository extends JpaRepository<ShakeEntity,Long>, JpaSpecificationExecutor<ShakeEntity> {
}
