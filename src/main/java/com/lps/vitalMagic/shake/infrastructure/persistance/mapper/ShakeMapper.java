package com.lps.vitalMagic.shake.infrastructure.persistance.mapper;


import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.infrastructure.persistance.entity.ShakeEntity;

public class ShakeMapper {


    public static ShakeEntity toEntity(Shake domain) {
        return new ShakeEntity(domain.getId(), domain.getName(), domain.getDescription(),domain.getShakeType(),domain.getShakeCategory(),domain.getIngredients().stream().map(ShakeIngredientMapper::toEntity).toList(), domain.isActive());
    }


    public static Shake toDomain(ShakeEntity entity) {
        return Shake.from(entity.getId(), entity.getName(), entity.getDescription(),entity.getShakeType() ,entity.getShakeCategory(),entity.getIngredients().stream().map(ShakeIngredientMapper::toDomain).toList(),entity.isActive());
    }
}
