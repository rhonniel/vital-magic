package com.lps.vitalMagic.inventory.domain.repository;

import com.lps.vitalMagic.inventory.domain.model.entity.Item;

import java.util.List;
import java.util.Optional;


public interface ItemRepository {
    Optional<Item> findById(Long id);
    Item save(Item item);
    List<Item> findAllAvailableItems();
}
