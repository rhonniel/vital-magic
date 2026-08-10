package com.lps.vitalMagic.shake.application.service;

import com.lps.vitalMagic.common.exception.ResourceNotFoundException;
import com.lps.vitalMagic.shake.application.query.ShakeIngredientSource;
import com.lps.vitalMagic.shake.application.query.ShakeProductSource;
import com.lps.vitalMagic.shake.application.usecase.FindShakeProductSourceUseCase;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.repository.ShakeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindShakeProductSourceService implements FindShakeProductSourceUseCase {

    private final ShakeRepository shakeRepository;

    public FindShakeProductSourceService(ShakeRepository shakeRepository) {
        this.shakeRepository = shakeRepository;
    }

    @Override
    public ShakeProductSource execute(Long shakeId) {
        Shake shake = shakeRepository.findById(shakeId)
                .orElseThrow(() -> new ResourceNotFoundException("Shake",shakeId));

        List<ShakeIngredientSource> ingredients =
                shake.getIngredients().stream()
                        .map(ingredient ->
                                new ShakeIngredientSource(
                                        ingredient.getItemId(),
                                        ingredient.getQuantity()
                                )
                        )
                        .toList();

        return new ShakeProductSource(
                shake.getId(),
                shake.getName(),
                ingredients
        );
    }
}