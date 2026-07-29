package com.lps.vitalMagic.inventory.application.service;

import com.lps.vitalMagic.inventory.application.dto.ItemInfo;
import com.lps.vitalMagic.common.presentation.exception.ResourceNotFoundException;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import org.springframework.stereotype.Service;

@Service
public class FindItemService {
    private final ItemRepository itemRepository;

    public FindItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemInfo getItemInfo(Long itemId){
        Item item=itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", itemId));
        return new ItemInfo(item.getId(),item.getName());
    }
}
