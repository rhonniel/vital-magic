package com.lps.vitalMagic.product.domain.service;

import com.lps.vitalMagic.inventory.application.service.ItemCurrentStockService;
import com.lps.vitalMagic.product.domain.model.data.Composition;
import com.lps.vitalMagic.product.domain.model.data.IngredientComposition;
import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.model.entity.ShakeIngredient;
import com.lps.vitalMagic.shake.domain.repository.ShakeRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductAvailabilityService {

    private final ShakeRepository shakeRepository;
    private final ItemCurrentStockService itemCurrentStockService;


    public ProductAvailabilityService(ShakeRepository shakeRepository, ItemCurrentStockService itemCurrentStockService) {
        this.shakeRepository = shakeRepository;
        this.itemCurrentStockService = itemCurrentStockService;
    }

    public boolean checkAvailability(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }

        return switch (product.getProductType()) {
            case SHAKE ->
                    checkShakeAvailability(
                            product.getReferenceNo(),
                            quantity
                    );

            case SIMPLE_PRODUCT ->
                    checkSimpleProductAvailability(
                            product.getReferenceNo(),
                            quantity
                    );
        };

    }

    private boolean checkShakeAvailability(Long shakeId, int quantity) {

        Shake shake = shakeRepository.findById(shakeId).orElseThrow(IllegalStateException::new);

        for(ShakeIngredient shakeIngredient:shake.getIngredients()){
           Integer currentStock= itemCurrentStockService.getCurrentStock(shakeIngredient.getItemId());
           Integer newConsume= shakeIngredient.getQuantity()*quantity;
           if (currentStock<newConsume){
               return false;
           }
        }

        return true;
    }


    private boolean checkSimpleProductAvailability(Long itemId, int quantity) {
        Integer currentStock= itemCurrentStockService.getCurrentStock(itemId);
        return currentStock >= quantity;
    }

    public boolean checkAvailability(Composition composition) {

        for(IngredientComposition ingredient:composition.items()){
            Integer currentStock= itemCurrentStockService.getCurrentStock(ingredient.itemId());
            Integer newConsume= ingredient.quantity();
            if (currentStock<newConsume){
                return false;
            }
        }
        return true;
    }
}
