package com.lps.vitalMagic.shake.domain.model.entity;

import com.lps.vitalMagic.shake.domain.model.exception.InvalidShakeException;
import lombok.Getter;

import java.util.Objects;


@Getter
public class ShakeIngredient {

    private Long itemId;

    private int quantity;

     ShakeIngredient(Long itemId, int quantity) {

         if(quantity<=0){
             throw  new InvalidShakeException("A Shake Ingredient must contain at least one unit");
         }
        this.itemId = Objects.requireNonNull(itemId);
        this.quantity = quantity;
    }

    private ShakeIngredient() {
    }

    public static ShakeIngredient from(Long itemId, int quantity){
        ShakeIngredient ingredient = new ShakeIngredient();
        ingredient.itemId = itemId;
        ingredient.quantity = quantity;

        return ingredient;
    }





}
