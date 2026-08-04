package com.lps.vitalMagic.inventory.infrastructure.persistence.repository.impl;

import com.lps.vitalMagic.inventory.application.query.SearchItemsQuery;
import com.lps.vitalMagic.inventory.application.view.ItemView;
import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.mapper.ItemMapper;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.specification.ItemSpecifications;
import com.lps.vitalMagic.common.presentation.pagination.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class JpaItemRepository implements ItemRepository {

    private final ItemJpaRepository jpaRepo;
    private final JpaAttributeRepository attributeRepository;

    public JpaItemRepository(ItemJpaRepository jpaRepo, JpaAttributeRepository attributeRepository) {
        this.jpaRepo = jpaRepo;
        this.attributeRepository = attributeRepository;
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
    public PageResult<ItemView> searchAvailableItems(SearchItemsQuery query) {

        Pageable pageable = PageRequest.of(
                query.pagination().page(),
                query.pagination().size()
        );

        Page<ItemEntity> page = jpaRepo.findAll(
                ItemSpecifications.withFilters(query),
                pageable
        );

        Map<Long, String> attributeNames = attributeRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Attribute::getId,
                        Attribute::getName
                ));


        return ItemMapper.toPageResult(page,attributeNames);

    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepo.existsById(id);
    }

    @Override
    public List<Item> findAllById(Set<Long> itemIds) {
        return jpaRepo.findAllById(itemIds).stream().map(ItemMapper::toDomain).toList();
    }


}