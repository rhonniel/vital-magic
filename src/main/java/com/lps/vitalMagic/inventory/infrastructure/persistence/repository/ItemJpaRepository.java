package com.lps.vitalMagic.inventory.infrastructure.persistence.repository;

import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ItemJpaRepository extends JpaRepository<ItemEntity,Long>, JpaSpecificationExecutor<ItemEntity> {

    List<ItemEntity> findByActiveTrue();
}
