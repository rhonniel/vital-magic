package com.lps.vitalMagic.shake.application.service;

import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemAttribute;
import com.lps.vitalMagic.inventory.domain.repository.AttributeRepository;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import com.lps.vitalMagic.shake.application.query.SearchShakeQuery;
import com.lps.vitalMagic.shake.application.usecase.SearchShakeUseCase;
import com.lps.vitalMagic.shake.application.view.ShakeAttributeView;
import com.lps.vitalMagic.shake.application.view.ShakeIngredientView;
import com.lps.vitalMagic.shake.application.view.ShakeView;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.model.entity.ShakeIngredient;
import com.lps.vitalMagic.shake.domain.repository.ShakeRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SearchShakeService implements SearchShakeUseCase {

    private final ShakeRepository shakeRepository;
    private final ItemRepository itemRepository;
    private final AttributeRepository attributeRepository;

    public SearchShakeService(ShakeRepository shakeRepository, ItemRepository itemRepository, AttributeRepository attributeRepository) {
        this.shakeRepository = shakeRepository;
        this.itemRepository = itemRepository;
        this.attributeRepository = attributeRepository;
    }

    @Override
    public List<ShakeView> execute(SearchShakeQuery query) {

        List<Shake> shakes= shakeRepository.searchAvailableShakes(query);
        Set<Long> itemIds= new HashSet<>();


        shakes.forEach(shake -> {
            shake.getIngredients().forEach(shakeIngredient -> {
                itemIds.add(shakeIngredient.getItemId());
            });
        });

        Map<Long,Item> itemHashMap= itemRepository.findAllById(itemIds)
                .stream()
                .collect(Collectors.toMap(
                        Item::getId,
                        Function.identity()
                ));


        Map<Long, Attribute> attributeHashMap=attributeRepository.findAll()
                .stream().collect(Collectors.toMap(Attribute::getId, Function.identity()));




        return shakes.stream()
                .map(shake -> toView(shake, itemHashMap,attributeHashMap))
                .toList();

    }


    private ShakeView toView(Shake shake, Map<Long,Item> itemMap, Map<Long,Attribute>attributeHashMap) {

        List<ShakeAttributeView> attributes = new ArrayList<>();
        List<ShakeIngredientView> ingredients= new ArrayList<>();

        Map<Long,Integer> attributesTotalMap = new HashMap<>();
        for(ShakeIngredient ingredient:shake.getIngredients()){

           for(ItemAttribute itemAttribute:itemMap.get(ingredient.getItemId()).getAttributes()){

               if(attributesTotalMap.containsKey(itemAttribute.getAttributeId())){
                   attributesTotalMap.compute(itemAttribute.getAttributeId(), (k, value) -> value + itemAttribute.getValue());
               }else {
                   attributesTotalMap.put(itemAttribute.getAttributeId(),itemAttribute.getValue());
               }

           }

            ShakeIngredientView ingredientView =new ShakeIngredientView(
                    ingredient.getItemId(),
                    itemMap.get(ingredient.getItemId()).getName(),
                    ingredient.getQuantity());
            ingredients.add(ingredientView);
        }

        for (var entry : attributesTotalMap.entrySet()) {

            attributes.add(
                    new ShakeAttributeView(
                            entry.getKey(),
                            attributeHashMap.get(entry.getKey()).getName(),
                            entry.getValue()
                    )
            );
        }


        return new ShakeView(
                shake.getId(),
                shake.getName(),
                shake.getDescription(),
                shake.getShakeType(),
                shake.getShakeCategory(),
                attributes,
                ingredients
        );
    }

}
