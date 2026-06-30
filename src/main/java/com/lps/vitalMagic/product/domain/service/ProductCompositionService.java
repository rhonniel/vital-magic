package com.lps.vitalMagic.product.domain.service;

import com.lps.vitalMagic.product.domain.model.data.Composition;
import com.lps.vitalMagic.product.domain.model.data.IngredientComposition;
import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.model.entity.ShakeIngredient;
import com.lps.vitalMagic.shake.domain.repository.ShakeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductCompositionService {

    private final ShakeRepository shakeRepository;

    public ProductCompositionService(ShakeRepository shakeRepository) {
        this.shakeRepository = shakeRepository;
    }

    public Composition getComposition(Product product, int quantity){
        List<IngredientComposition> ingredientCompositions= new ArrayList<>();
        Composition composition = new Composition(ingredientCompositions);

        if(product.getProductType()== ProductType.SHAKE){
            Shake shake = shakeRepository.findById(product.getReferenceNo()).orElseThrow(EntityNotFoundException::new);
            for(ShakeIngredient shakeIngredient:shake.getIngredients()){
                ingredientCompositions.add(new IngredientComposition(shakeIngredient.getItemId(),shakeIngredient.getQuantity()*quantity));
            }
        }else{
            ingredientCompositions.add(new IngredientComposition(product.getReferenceNo(), quantity));
        }

        return composition;


    }


}
