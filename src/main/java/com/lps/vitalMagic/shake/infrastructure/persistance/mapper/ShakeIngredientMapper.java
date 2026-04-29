package com.lps.vitalMagic.shake.infrastructure.persistance.mapper;


import com.lps.vitalMagic.shake.domain.model.entity.ShakeIngredient;
import com.lps.vitalMagic.shake.infrastructure.persistance.entity.ShakeIngredientEntity;

public class ShakeIngredientMapper  {

    public static ShakeIngredientEntity toEntity(ShakeIngredient domain) {
        return  new ShakeIngredientEntity(domain.getItemId(), domain.getShakeId(), domain.getQuantity());
    }


    public static ShakeIngredient toDomain(ShakeIngredientEntity entity) {
        return  ShakeIngredient.from(entity.getShakeId(), entity.getItemId(), entity.getQuantity());
    }
}
