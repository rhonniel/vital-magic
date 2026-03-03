package com.lps.vitalMagic.inventory.repository;

import com.lps.vitalMagic.inventory.model.entity.Item;

import java.util.List;
import java.util.Optional;


public interface ItemRepository {
    Optional<Item> findById(Long id);
    Item save(Item item);
    List<Item> findAllAvailableItems();
}
