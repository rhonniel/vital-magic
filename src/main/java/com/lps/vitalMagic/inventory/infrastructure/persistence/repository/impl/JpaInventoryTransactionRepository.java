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
    private final InventoryTransactionMapper mapper;


    public JpaInventoryTransactionRepository(InventoryTransactionJpaRepository jpaRepository, InventoryTransactionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<InventoryTransaction> findAllUnprocessedTransactions() {
        return jpaRepository.findAllUnprocessedTransactions().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<InventoryTransaction> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<InventoryTransaction> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public InventoryTransaction save(InventoryTransaction inventoryTransaction) {
        InventoryTransactionEntity entity= jpaRepository.save(mapper.toEntity(inventoryTransaction));
        return mapper.toDomain(entity);
    }
}
