package com.lps.vitalMagic.shake.application.service;

import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import com.lps.vitalMagic.product.aplication.command.CreateShakeProductCommand;
import com.lps.vitalMagic.product.aplication.usecase.CreateShakeProductUseCase;
import com.lps.vitalMagic.shake.application.command.CreateShakeIngredientCommand;
import com.lps.vitalMagic.shake.application.command.CreateStandardShakeCommand;
import com.lps.vitalMagic.shake.application.usecase.CreateStandardShakeUseCase;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.model.input.IngredientQuantityInput;
import com.lps.vitalMagic.shake.domain.repository.ShakeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreateStandardShakeService implements CreateStandardShakeUseCase {

    private final ShakeRepository shakeRepository;
    private final ItemRepository itemRepository; //TODO I already that dependency is too deep , is better call a service or use case, but i fix this in the another stage
    private final CreateShakeProductUseCase createShakeProductUseCase;


    public CreateStandardShakeService(ShakeRepository shakeRepository, ItemRepository itemRepository, CreateShakeProductUseCase createShakeProductUseCase) {
        this.shakeRepository = shakeRepository;
        this.itemRepository = itemRepository;
        this.createShakeProductUseCase = createShakeProductUseCase;
    }

    @Override
    public Long execute(CreateStandardShakeCommand command) {

        List<IngredientQuantityInput> ingredientQuantityInputs = new ArrayList<>();

        for(CreateShakeIngredientCommand ingredientCommand: command.ingredients()){

            if(!itemRepository.existsById(ingredientCommand.ingredientId())){
                throw new EntityNotFoundException(
                        "Ingredient with id %d not found".formatted(ingredientCommand.ingredientId()));
            }

            ingredientQuantityInputs.add(new IngredientQuantityInput(ingredientCommand.ingredientId(),ingredientCommand.quantity()));

        }

       Shake standardShake= Shake.createStandardShake(command.name(), command.description(), command.shakeCategory(),ingredientQuantityInputs);

        Shake shake= shakeRepository.save(standardShake);

        createShakeProductUseCase.execute(new CreateShakeProductCommand(shake.getId()));

        return shake.getId();
    }
}
