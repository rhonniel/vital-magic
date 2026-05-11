package com.lps.vitalMagic.inventory.application.service;

import com.lps.vitalMagic.inventory.application.command.CreateItemAttributeCommand;
import com.lps.vitalMagic.inventory.application.command.CreateItemCommand;
import com.lps.vitalMagic.inventory.application.usecase.CreateItemUseCase;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemInventory;
import com.lps.vitalMagic.inventory.domain.model.input.AttributeValue;
import com.lps.vitalMagic.inventory.domain.repository.AttributeRepository;
import com.lps.vitalMagic.inventory.domain.repository.ItemInventoryRepository;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CreateItemService implements CreateItemUseCase {

    public final AttributeRepository attributeRepository;

    public final ItemRepository itemRepository;

    public final ItemInventoryRepository itemInventoryRepository;


    public CreateItemService(AttributeRepository attributeRepository, ItemRepository itemRepository, ItemInventoryRepository itemInventoryRepository) {
        this.attributeRepository = attributeRepository;
        this.itemRepository = itemRepository;
        this.itemInventoryRepository = itemInventoryRepository;
    }

    @Override
    public Long execute(CreateItemCommand command) {
         List<AttributeValue> attributeValues= new ArrayList<>();

        for(CreateItemAttributeCommand attribute:command.attributes()){
            if (!attributeRepository.existsById(attribute.attributeId())) {
                throw new EntityNotFoundException(
                        "Attribute with id %d not found".formatted(attribute.attributeId())
                );
            }
            attributeValues.add(new AttributeValue(attribute.attributeId(),attribute.value()));
        }
        Item  item= itemRepository.save(Item.create(command.name(), command.description(),attributeValues));

        // Se incluye la creacion del ItemIventory, para este MVP un item no puede existir sin  inventario

        ItemInventory itemInventory=ItemInventory.create(item.getId(),command.minStock());
        itemInventoryRepository.save(itemInventory);

        return item.getId();
    }
}
