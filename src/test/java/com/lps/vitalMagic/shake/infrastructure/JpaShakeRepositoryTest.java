package com.lps.vitalMagic.shake.infrastructure;

import com.lps.vitalMagic.common.pagination.PageResult;
import com.lps.vitalMagic.common.pagination.Pagination;
import com.lps.vitalMagic.config.MySqlDataJpaTest;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.AttributeEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemAttributeEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.AttributeJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemJpaRepository;
import com.lps.vitalMagic.shake.application.query.SearchShakeQuery;
import com.lps.vitalMagic.shake.application.view.ShakeView;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;
import com.lps.vitalMagic.shake.infrastructure.persistence.entity.ShakeEntity;
import com.lps.vitalMagic.shake.infrastructure.persistence.entity.ShakeIngredientEntity;
import com.lps.vitalMagic.shake.infrastructure.persistence.repository.ShakeEntityJpaRepository;
import com.lps.vitalMagic.shake.infrastructure.persistence.repository.impl.JpaShakeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(JpaShakeRepository.class)
class JpaShakeRepositoryTest extends MySqlDataJpaTest {

    private static final Long STRENGTH_ATTRIBUTE_ID = 2L;
    private static final Long MAGIC_ATTRIBUTE_ID = 3L;

    @Autowired
    private JpaShakeRepository repository;

    @Autowired
    private ShakeEntityJpaRepository shakeJpaRepository;

    @Autowired
    private ItemJpaRepository itemRepository;

    @Autowired
    private AttributeJpaRepository attributeRepository;

    @Test
    void searchAvailableShakesShouldKeepIngredientsAndAttributesSeparatedByShake() {

        saveAttributes();

        ItemEntity dragonTail = saveItemWithAttributes(
                "Dragon Tail",
                List.of(
                        new ItemAttributeEntity(
                                STRENGTH_ATTRIBUTE_ID,
                                3
                        )
                )
        );

        ItemEntity fairyDust = saveItemWithAttributes(
                "Fairy Dust",
                List.of(
                        new ItemAttributeEntity(
                                MAGIC_ATTRIBUTE_ID,
                                4
                        )
                )
        );

        ShakeEntity dragonShake = saveShake(
                "Dragon Power",
                List.of(
                        new ShakeIngredientEntity(
                                dragonTail.getId(),
                                2
                        )
                )
        );

        ShakeEntity fairyShake = saveShake(
                "Fairy Power",
                List.of(
                        new ShakeIngredientEntity(
                                fairyDust.getId(),
                                3
                        )
                )
        );

        SearchShakeQuery query = new SearchShakeQuery(
                null,
                null,
                new Pagination(0, 10)
        );

        PageResult<ShakeView> result = repository.searchAvailableShakes(query);

        assertEquals(2, result.content().size());

        ShakeView dragonView = findShake(result.content(), dragonShake.getId());

        ShakeView fairyView = findShake(result.content(), fairyShake.getId());

        assertEquals(1, dragonView.ingredients().size());
        assertEquals(
                dragonTail.getId(),
                dragonView.ingredients().get(0).ingredientId()
        );

        assertEquals(1, dragonView.attributes().size());
        assertEquals(
                STRENGTH_ATTRIBUTE_ID,
                dragonView.attributes().get(0).attributeId()
        );

        assertEquals(1, fairyView.ingredients().size());
        assertEquals(fairyDust.getId(), fairyView.ingredients().get(0).ingredientId());

        assertEquals(1, fairyView.attributes().size());
        assertEquals(MAGIC_ATTRIBUTE_ID, fairyView.attributes().get(0).attributeId());
    }

    private ShakeView findShake(List<ShakeView> shakes, Long shakeId) {
        return shakes.stream()
                .filter(shake -> shake.id().equals(shakeId))
                .findFirst()
                .orElseThrow();
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

    private ItemEntity saveItemWithAttributes(String name, List<ItemAttributeEntity> attributes) {
        ItemEntity item = new ItemEntity(
                name,
                name + " description",
                true
        );

        attributes.forEach(item::addAttribute);

        return itemRepository.saveAndFlush(item);
    }

    private ShakeEntity saveShake(String name, List<ShakeIngredientEntity> ingredients) {
        ShakeEntity shake = new ShakeEntity(
                null,
                name,
                name + " description",
                ShakeType.STANDARD,
                ShakeCategory.HEALTH,
                true
        );

        ingredients.forEach(
                shake::addShakeIngredientEntity
        );

        return shakeJpaRepository.saveAndFlush(shake);
    }
}