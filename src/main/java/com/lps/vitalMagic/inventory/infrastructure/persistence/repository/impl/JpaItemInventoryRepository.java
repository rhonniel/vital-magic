package com.lps.vitalMagic.inventory.infrastructure.persistence.repository.impl;

import com.lps.vitalMagic.inventory.domain.model.entity.ItemInventory;
import com.lps.vitalMagic.inventory.domain.repository.ItemInventoryRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemInventoryEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.mapper.ItemInventoryMapper;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemInventoryJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaItemInventoryRepository implements ItemInventoryRepository {

    private final ItemInventoryMapper mapper;
    private final ItemInventoryJpaRepository jpaRepository;

    public JpaItemInventoryRepository(ItemInventoryMapper mapper, ItemInventoryJpaRepository jpaRepository) {
        this.mapper = mapper;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<ItemInventory> findAllActive() {
        return jpaRepository.findByActiveTrue()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ItemInventory> findById(Long id) {

        return  jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<ItemInventory> findItemsWithLowStock() {
        return jpaRepository.findItemsWithLowStock()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public ItemInventory save(ItemInventory itemInventory) {
        ItemInventoryEntity entity = mapper.toEntity(itemInventory);

        ItemInventoryEntity savedEntity = jpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }
}
