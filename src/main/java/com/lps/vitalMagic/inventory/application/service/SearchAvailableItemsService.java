package com.lps.vitalMagic.inventory.application.service;

import com.lps.vitalMagic.inventory.application.query.SearchItemsQuery;
import com.lps.vitalMagic.inventory.application.view.ItemAttributeView;
import com.lps.vitalMagic.inventory.application.view.ItemView;
import com.lps.vitalMagic.inventory.application.usecase.SearchAvailableItemsUseCase;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchAvailableItemsService implements SearchAvailableItemsUseCase {
    private final ItemRepository itemRepository;


    public SearchAvailableItemsService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<ItemView> execute(SearchItemsQuery query) {

       List<Item> items= itemRepository.searchAvailableItems(query);

        return items.stream().map(item -> {
            List<ItemAttributeView> attributeViews= item.getAttributes().stream()
                    .map(attribute -> new ItemAttributeView(attribute.getAttributeId(),"",attribute.getValue())).toList();
            return new ItemView(item.getId(),item.getName(),item.getDescription(),attributeViews);
        }).toList();

    }
}
