package com.lps.vitalMagic.shake.infrastructure.persistance.repository;

import com.lps.vitalMagic.shake.infrastructure.persistance.entity.ShakeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShakeEntityJpaRepository extends JpaRepository<ShakeEntity,Long> {
}
