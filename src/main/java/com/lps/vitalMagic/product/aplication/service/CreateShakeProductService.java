package com.lps.vitalMagic.product.aplication.service;

import com.lps.vitalMagic.inventory.application.service.ItemCostProvider;
import com.lps.vitalMagic.product.aplication.command.CreateShakeProductCommand;
import com.lps.vitalMagic.product.aplication.exception.ProductAlreadyExistsException;
import com.lps.vitalMagic.product.aplication.usecase.CreateShakeProductUseCase;
import com.lps.vitalMagic.product.domain.model.data.IngredientCost;
import com.lps.vitalMagic.product.domain.model.data.ShakeProductData;
import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.product.domain.repository.ProductRepository;
import com.lps.vitalMagic.shake.application.query.ShakeIngredientSource;
import com.lps.vitalMagic.shake.application.query.ShakeProductSource;
import com.lps.vitalMagic.shake.application.usecase.FindShakeProductSourceUseCase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
//* TODO Los mensages de nigun fallo salen solo dice la excepcion pero no dice el mensage de que paso
@Service
public class CreateShakeProductService implements CreateShakeProductUseCase {

    private final ProductRepository productRepository;
    private final FindShakeProductSourceUseCase findShakeProductSourceUseCase;
    private final ItemCostProvider itemCostProvider;

    public CreateShakeProductService(ProductRepository productRepository, FindShakeProductSourceUseCase
                                        findShakeProductSourceUseCase, ItemCostProvider itemCostProvider) {
        this.productRepository = productRepository;
        this.findShakeProductSourceUseCase = findShakeProductSourceUseCase;
        this.itemCostProvider = itemCostProvider;
    }

    @Override
    public Long execute(CreateShakeProductCommand command) {

        validateProductDoesNotExist(command);

        ShakeProductSource shake = findShakeProductSourceUseCase.execute(command.referenceNo());

        Set<Long> itemIds = shake.ingredients().stream()
                .map(ShakeIngredientSource::itemId)
                .collect(Collectors.toSet());

        Map<Long, BigDecimal> itemCosts = itemCostProvider.findCostsByItemIds(itemIds);

        List<IngredientCost> ingredientCosts =
                shake.ingredients().stream()
                        .map(ingredient ->
                                new IngredientCost(
                                        ingredient.itemId(),
                                        ingredient.quantity(),
                                        requireItemCost(
                                                ingredient.itemId(),
                                                itemCosts
                                        )
                                )
                        )
                        .toList();

        ShakeProductData productData =
                new ShakeProductData(
                        shake.shakeId(),
                        shake.name(),
                        ingredientCosts
                );

        Product product= Product.createShakeProduct(productData);


        return productRepository.save(product).getId();
    }

    private BigDecimal requireItemCost(Long itemId, Map<Long, BigDecimal> itemCosts) {
        BigDecimal cost = itemCosts.get(itemId);

        if (cost == null) {
            throw new IllegalStateException("Item does not have cost");
        }

        return cost;
    }

    private void validateProductDoesNotExist( CreateShakeProductCommand command) {
        boolean alreadyExists =
                productRepository
                        .existsByReferenceNoAndProductType(
                                command.referenceNo(),
                                 ProductType.SHAKE
                        );

        if (alreadyExists) {
            throw new ProductAlreadyExistsException(
                    command.referenceNo(),
                    ProductType.SHAKE
            );
        }
    }
}