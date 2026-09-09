package com.lps.vitalMagic.shake.infrastructure.persistence.mapper;


import com.lps.vitalMagic.common.pagination.PageResult;
import com.lps.vitalMagic.shake.application.view.ShakeAttributeView;
import com.lps.vitalMagic.shake.application.view.ShakeIngredientView;
import com.lps.vitalMagic.shake.application.view.ShakeView;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.infrastructure.persistence.entity.ShakeEntity;
import com.lps.vitalMagic.shake.infrastructure.persistence.projection.ShakeAttributeProjection;
import com.lps.vitalMagic.shake.infrastructure.persistence.projection.ShakeIngredientProjection;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Objects;

public class ShakeMapper {


    public static ShakeEntity toEntity(Shake domain) {
        ShakeEntity entity=new ShakeEntity(domain.getId(), domain.getName(), domain.getDescription(),domain.getShakeType(),
                domain.getShakeCategory(), domain.isActive());
        domain.getIngredients().forEach(shakeIngredient -> {
            entity.addShakeIngredientEntity(ShakeIngredientMapper.toEntity(shakeIngredient));
        });

        return entity;
    }


    public static Shake toDomain(ShakeEntity entity) {
        return Shake.from(entity.getId(), entity.getName(), entity.getDescription(),entity.getShakeType() ,entity.getShakeCategory(),entity.getIngredients().stream().map(ShakeIngredientMapper::toDomain).toList(),entity.isActive());
    }

    public static PageResult<ShakeView> toPageResult(Page<ShakeEntity> page, List<ShakeAttributeProjection>attributes,
                                                     List<ShakeIngredientProjection> ingredients) {
        Page<ShakeView> viewPage =
                page.map(entity -> {
                          return new ShakeView(
                            entity.getId(),
                            entity.getName(),
                            entity.getDescription(),
                            entity.getShakeType(),
                            entity.getShakeCategory(),
                            attributes.stream().filter(shakeAttributeProjection ->
                                    Objects.equals(shakeAttributeProjection.shakeId(), entity.getId()))
                                    .map(projection ->
                                    new ShakeAttributeView(projection.attributeId(), projection.attributeName(),
                                            projection.total().intValueExact())).toList(),
                            ingredients.stream().filter(shakeAttributeProjection ->
                                    Objects.equals(shakeAttributeProjection.shakeId(), entity.getId()))
                                    .map(projection ->
                                    new ShakeIngredientView(projection.itemId(), projection.itemName(),
                                            projection.quantity())).toList()
                    );
                });

        return new PageResult<>(
                viewPage.getContent(),
                viewPage.getNumber(),
                viewPage.getSize(),
                viewPage.getTotalElements(),
                viewPage.getTotalPages()
        );
    }
}
