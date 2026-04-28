package com.lps.vitalMagic.inventory.infrastructure.persistence.repository.impl;

import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import com.lps.vitalMagic.inventory.domain.repository.InventoryTransactionRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.InventoryTransactionEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.mapper.InventoryTransactionMapper;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.InventoryTransactionJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaInventoryTransactionRepository implements InventoryTransactionRepository {

    private final InventoryTransactionJpaRepository jpaRepository;



    public JpaInventoryTransactionRepository(InventoryTransactionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<InventoryTransaction> findAllUnprocessedTransactions() {
        return jpaRepository.findAllUnprocessedTransactions().stream().map(InventoryTransactionMapper::toDomain).toList();
    }

    @Override
    public List<InventoryTransaction> findAll() {
        return jpaRepository.findAll().stream().map(InventoryTransactionMapper::toDomain).toList();
    }

    @Override
    public Optional<InventoryTransaction> findById(Long id) {
        return jpaRepository.findById(id).map(InventoryTransactionMapper::toDomain);
    }

    @Override
    public InventoryTransaction save(InventoryTransaction inventoryTransaction) {
        InventoryTransactionEntity entity= jpaRepository.save(InventoryTransactionMapper.toEntity(inventoryTransaction));
        return InventoryTransactionMapper.toDomain(entity);
    }
}
