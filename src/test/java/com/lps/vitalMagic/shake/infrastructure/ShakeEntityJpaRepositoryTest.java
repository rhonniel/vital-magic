package com.lps.vitalMagic.shake.infrastructure;


import com.lps.vitalMagic.config.MySqlDataJpaTest;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.AttributeEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemAttributeEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.AttributeJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemJpaRepository;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;
import com.lps.vitalMagic.shake.infrastructure.persistence.entity.ShakeEntity;
import com.lps.vitalMagic.shake.infrastructure.persistence.entity.ShakeIngredientEntity;
import com.lps.vitalMagic.shake.infrastructure.persistence.projection.ShakeAttributeProjection;
import com.lps.vitalMagic.shake.infrastructure.persistence.projection.ShakeIngredientProjection;
import com.lps.vitalMagic.shake.infrastructure.persistence.repository.ShakeEntityJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ShakeEntityJpaRepositoryTest extends MySqlDataJpaTest {

    private static final Long STRENGTH_ATTRIBUTE_ID = 2L;
    private static final Long MAGIC_ATTRIBUTE_ID = 3L;

    @Autowired
    private ShakeEntityJpaRepository shakeRepository;

    @Autowired
    private ItemJpaRepository itemRepository;

    @Autowired
    private AttributeJpaRepository attributeRepository;

    @Test
    void shouldFindIngredientsByShakeIds() {
        ItemEntity dragonTail = saveItem("Dragon Tail");
        ItemEntity fairyDust = saveItem("Fairy Dust");
        ItemEntity trollTooth = saveItem("Troll Tooth");

        ShakeEntity selectedShake = saveShake(
                "Dragon Power",
                List.of(
                        new ShakeIngredientEntity(
                                dragonTail.getId(),
                                2
                        ),
                        new ShakeIngredientEntity(
                                fairyDust.getId(),
                                3
                        )
                )
        );

        saveShake(
                "Excluded Shake",
                List.of(
                        new ShakeIngredientEntity(
                                trollTooth.getId(),
                                5
                        )
                )
        );

        List<ShakeIngredientProjection> result =
                shakeRepository.findIngredientsByShakeIds(
                        List.of(selectedShake.getId())
                );

        assertEquals(2, result.size());

        ShakeIngredientProjection dragonTailProjection =
                findIngredient(result, dragonTail.getId());

        ShakeIngredientProjection fairyDustProjection =
                findIngredient(result, fairyDust.getId());

        assertEquals(
                new ShakeIngredientProjection(
                        selectedShake.getId(),
                        dragonTail.getId(),
                        "Dragon Tail",
                        2
                ),
                dragonTailProjection
        );

        assertEquals(
                new ShakeIngredientProjection(
                        selectedShake.getId(),
                        fairyDust.getId(),
                        "Fairy Dust",
                        3
                ),
                fairyDustProjection
        );
    }

    @Test
    void shouldCalculateAttributesByShakeIds() {
        saveAttributes();

        ItemEntity dragonTail = saveItemWithAttributes(
                "Dragon Tail",
                List.of(
                        new ItemAttributeEntity(
                                STRENGTH_ATTRIBUTE_ID,
                                3
                        ),
                        new ItemAttributeEntity(
                                MAGIC_ATTRIBUTE_ID,
                                2
                        )
                )
        );

        ItemEntity fairyDust = saveItemWithAttributes(
                "Fairy Dust",
                List.of(
                        new ItemAttributeEntity(
                                STRENGTH_ATTRIBUTE_ID,
                                1
                        ),
                        new ItemAttributeEntity(
                                MAGIC_ATTRIBUTE_ID,
                                4
                        )
                )
        );

        ShakeEntity selectedShake = saveShake(
                "Dragon Power",
                List.of(
                        new ShakeIngredientEntity(
                                dragonTail.getId(),
                                2
                        ),
                        new ShakeIngredientEntity(
                                fairyDust.getId(),
                                3
                        )
                )
        );

        saveShake(
                "Excluded Shake",
                List.of(
                        new ShakeIngredientEntity(
                                dragonTail.getId(),
                                10
                        )
                )
        );

        List<ShakeAttributeProjection> result =
                shakeRepository.findAttributeByShakeIds(
                        List.of(selectedShake.getId())
                );
        assertEquals(2, result.size());

        ShakeAttributeProjection strength =
                findAttribute(result, STRENGTH_ATTRIBUTE_ID);

        ShakeAttributeProjection magic =
                findAttribute(result, MAGIC_ATTRIBUTE_ID);

        assertEquals("Strength", strength.attributeName());
        assertEquals(9, strength.total().intValueExact());

        assertEquals("Magic", magic.attributeName());
        assertEquals(16, magic.total().intValueExact());
    }

    private void saveAttributes() {
        attributeRepository.saveAllAndFlush(
                List.of(
                        new AttributeEntity(
                                STRENGTH_ATTRIBUTE_ID,
                                "Strength",
                                "Physical strength",
                                "STR"
                        ),
                        new AttributeEntity(
                                MAGIC_ATTRIBUTE_ID,
                                "Magic",
                                "Magical power",
                                "MAG"
                        )
                )
        );
    }

    private ItemEntity saveItem(String name) {
        return itemRepository.saveAndFlush(
                new ItemEntity(
                        name,
                        name + " description",
                        true
                )
        );
    }

    private ItemEntity saveItemWithAttributes(
            String name,
            List<ItemAttributeEntity> attributes
    ) {
        ItemEntity item = new ItemEntity(
                name,
                name + " description",
                true
        );

        attributes.forEach(item::addAttribute);

        return itemRepository.saveAndFlush(item);
    }

    private ShakeEntity saveShake(
            String name,
            List<ShakeIngredientEntity> ingredients
    ) {
        ShakeEntity shake = new ShakeEntity(
                null,
                name,
                name + " description",
                ShakeType.STANDARD,
                ShakeCategory.HEALTH,
                true
        );

        ingredients.forEach(shake::addShakeIngredientEntity);

        return shakeRepository.saveAndFlush(shake);
    }

    private ShakeIngredientProjection findIngredient(
            List<ShakeIngredientProjection> projections,
            Long itemId
    ) {
        return projections.stream()
                .filter(projection ->
                        projection.itemId().equals(itemId)
                )
                .findFirst()
                .orElseThrow();
    }

    private ShakeAttributeProjection findAttribute(
            List<ShakeAttributeProjection> projections,
            Long attributeId
    ) {
        return projections.stream()
                .filter(projection ->
                        projection.attributeId().equals(attributeId)
                )
                .findFirst()
                .orElseThrow();
    }
}