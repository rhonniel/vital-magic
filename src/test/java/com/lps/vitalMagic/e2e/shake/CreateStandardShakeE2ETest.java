package com.lps.vitalMagic.e2e.shake;

import com.lps.vitalMagic.config.MySqlIntegrationTest;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemInventoryEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemInventoryJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemJpaRepository;
import com.lps.vitalMagic.shake.application.controller.ShakeController;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;
import com.lps.vitalMagic.shake.infrastructure.persistence.repository.ShakeEntityJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateStandardShakeE2ETest extends MySqlIntegrationTest {
    @Autowired private TestRestTemplate http;
    @Autowired private ItemJpaRepository items;
    @Autowired private ItemInventoryJpaRepository inventories;
    @Autowired private ShakeEntityJpaRepository shakes;
    @Autowired private JdbcTemplate jdbc;

    @AfterEach
    void cleanCommittedFixtures() {
        // Keep Flyway's attribute catalog; remove only the fixture tables in dependency order.
        jdbc.update("DELETE FROM product");
        jdbc.update("DELETE FROM shake_ingredient");
        jdbc.update("DELETE FROM shake");
        jdbc.update("DELETE FROM item_inventory");
        jdbc.update("DELETE FROM item");
    }

    @Test
    void shouldCreateStandardShakeWithIngredientsAndProduct() {
        Long first = saveIngredient("Dragon Tail", "2.00");
        Long second = saveIngredient("Moon Berry", "4.00");
        Long third = saveIngredient("Magic Milk", "3.00");
        Map<Long, Integer> ingredients = Map.of(first, 2, second, 3, third, 1);
        var request = new ShakeController.CreateShakeRequest(
                "Standard shake", "Description", ShakeCategory.RARE, ingredients);

        var response = http.postForEntity("/shake", request, ShakeController.CreateShakeResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        Long shakeId = response.getBody().id();
        assertNotNull(shakeId);
        var shake = shakes.findById(shakeId).orElseThrow();
        assertEquals(ShakeType.STANDARD, shake.getShakeType());
        // Query persisted children directly, without traversing a detached LAZY collection.
        var persistedIngredients = jdbc.query(
                "SELECT item_id, quantity FROM shake_ingredient WHERE shake_id = ?",
                (rs, rowNum) -> Map.entry(rs.getLong("item_id"), rs.getInt("quantity")), shakeId)
                .stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        assertEquals(ingredients, persistedIngredients);
        assertEquals(1, jdbc.queryForObject(
                "SELECT COUNT(*) FROM product WHERE reference_no = ? AND product_type = 'SHAKE' AND active = true",
                Integer.class, shakeId));
    }

    private Long saveIngredient(String name, String cost) {
        Long itemId = items.saveAndFlush(new ItemEntity(name, "Description", true)).getId();
        inventories.saveAndFlush(new ItemInventoryEntity(null, itemId, new BigDecimal(cost), 0, 0, true));
        return itemId;
    }
}
