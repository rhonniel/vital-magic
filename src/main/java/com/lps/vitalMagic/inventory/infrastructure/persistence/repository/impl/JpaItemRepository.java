package com.lps.vitalMagic.inventory.infrastructure.persistence.repository.impl;

import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.mapper.ItemMapper;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaItemRepository implements ItemRepository {

    private final ItemJpaRepository jpaRepo;


    public JpaItemRepository(
            ItemJpaRepository jpaRepo,
            ItemMapper mapper) {
        this.jpaRepo = jpaRepo;

    }
    @Override
    public Optional<Item> findById(Long id) {
        return jpaRepo.findById(id)
                .map(ItemMapper::toDomain);
    }

    @Override
    public Item save(Item item) {
        ItemEntity entity = ItemMapper.toEntity(item);

        ItemEntity savedEntity = jpaRepo.save(entity);

        return ItemMapper.toDomain(savedEntity);
    }

    @Override
    public List<Item> findAllAvailableItems() {

        return jpaRepo.findByActiveTrue()
                .stream()
                .map(ItemMapper::toDomain)
                .toList();
    }
}