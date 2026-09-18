package com.lps.vitalMagic.inventory.infrastructure.persistence.repository;


import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemInventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemInventoryJpaRepository extends JpaRepository<ItemInventoryEntity,Long> {
    List<ItemInventoryEntity> findByActiveTrue();

    @Query("Select i from ItemInventoryEntity i where i.currentStock<=i.minStock")
    List<ItemInventoryEntity> findItemsWithLowStock();

    Optional<ItemInventoryEntity> findByActiveTrueAndItemId(Long itemId);

    List<ItemInventoryEntity> findByItemIdIn(Collection<Long> itemIds);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update ItemInventoryEntity i set i.currentStock = i.currentStock + :quantity
            where i.itemId = :itemId and i.active = true
            """)
    int addToCurrentStock(@Param("itemId") Long itemId, @Param("quantity") int quantity);
}
