package com.lps.vitalMagic.shake.infrastructure.persistence.mapper;


import com.lps.vitalMagic.common.presentation.pagination.PageResult;
import com.lps.vitalMagic.shake.application.view.ShakeAttributeView;
import com.lps.vitalMagic.shake.application.view.ShakeIngredientView;
import com.lps.vitalMagic.shake.application.view.ShakeView;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.infrastructure.persistence.entity.ShakeEntity;
import com.lps.vitalMagic.shake.infrastructure.persistence.projection.ShakeAttributeProjection;
import com.lps.vitalMagic.shake.infrastructure.persistence.projection.ShakeIngredientProjection;
import org.springframework.data.domain.Page;

import java.util.List;

public class ShakeMapper {


    public static ShakeEntity toEntity(Shake domain) {
        return new ShakeEntity(domain.getId(), domain.getName(), domain.getDescription(),domain.getShakeType(),domain.getShakeCategory(),domain.getIngredients().stream().map(ShakeIngredientMapper::toEntity).toList(), domain.isActive());
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
                            attributes.stream().map(projection ->
                                    new ShakeAttributeView(projection.attributeId(), projection.attributeName(),
                                            projection.total())).toList(),
                            ingredients.stream().map(projection ->
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
