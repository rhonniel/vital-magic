package com.lps.vitalMagic.inventory.infrastructure.persistence.repository;

import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.InventoryTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryTransactionJpaRepository extends JpaRepository<InventoryTransactionEntity,Long> {
    @Query("select i from InventoryTransactionEntity i where i.processAt is null ")
    List<InventoryTransactionEntity> findAllUnprocessedTransactions();

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update InventoryTransactionEntity i set i.processAt = :processedAt
            where i.id in :transactionIds and i.processAt is null
            """)
    int markAsProcessed(@Param("transactionIds") List<Long> transactionIds,
                        @Param("processedAt") LocalDateTime processedAt);

    @Query("""
    select coalesce(
        sum(
            case
                when i.type = 'SALE' then -i.quantity
                when i.type = 'PURCHASE' then i.quantity
            end
        ),
        0
    )
    from InventoryTransactionEntity i
    where i.processAt is null
      and i.itemId = :id
""")
    Integer findTotalUnprocessedStocksByItemId(@Param("id")Long id);
}
