package com.lps.vitalMagic.product.domain.service;

import com.lps.vitalMagic.inventory.domain.service.ItemCurrentStockService;
import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.model.entity.ShakeIngredient;
import com.lps.vitalMagic.shake.domain.repository.ShakeRepository;
import jakarta.persistence.EntityNotFoundException;
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

       if(product.getProductType()== ProductType.SHAKE){
           return checkShakeAvailability(product.getReferenceNo(),quantity);
       }
       if(product.getProductType()== ProductType.SIMPLE_PRODUCT){
            return checkSimpleProductAvailability(product.getReferenceNo(),quantity);
        }


        return true;
    }

    private boolean checkShakeAvailability(Long shakeId, int quantity) {

        Shake shake = shakeRepository.findById(shakeId).orElseThrow(EntityNotFoundException::new);

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

}
