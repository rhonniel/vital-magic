package com.lps.vitalMagic.inventory.infrastructure.persistence.repository;

import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemInventoryJpaRepository extends JpaRepository<ItemInventoryEntity,Long> {
    List<ItemInventoryEntity> findByActiveTrue();

    @Query("Select i from ItemInventoryEntity i where i.currentStock<=i.minStock")
    List<ItemInventoryEntity> findItemsWithLowStock();

    Optional<ItemInventoryEntity> findByActiveTrueAndItemId(Long itemId);
}
