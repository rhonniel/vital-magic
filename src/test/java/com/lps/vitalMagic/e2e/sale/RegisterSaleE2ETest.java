package com.lps.vitalMagic.e2e.sale;

import com.lps.vitalMagic.config.MySqlIntegrationTest;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.InventoryTransactionJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemInventoryEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemInventoryJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemJpaRepository;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.product.infrastructure.persistance.entity.ProductEntity;
import com.lps.vitalMagic.product.infrastructure.persistance.repository.ProductEntityJpaRepository;
import com.lps.vitalMagic.sales.application.controller.SaleController;
import com.lps.vitalMagic.sales.infrastructure.persistence.repository.SaleJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterSaleE2ETest extends MySqlIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Autowired
    private ItemJpaRepository items;

    @Autowired
    private ItemInventoryJpaRepository inventories;

    @Autowired
    private ProductEntityJpaRepository products;

    @Autowired
    private SaleJpaRepository sales;

    @Autowired
    private InventoryTransactionJpaRepository transactions;

    @Autowired
    private JdbcTemplate jdbc;

    @AfterEach
    void cleanDatabase() {

        jdbc.update("DELETE FROM sale_item");
        jdbc.update("DELETE FROM sale");
        jdbc.update("DELETE FROM inventory_transaction");
        jdbc.update("DELETE FROM product");
        jdbc.update("DELETE FROM item_inventory");
        jdbc.update("DELETE FROM item");
    }

    @Test
    void shouldRegisterSaleAndConsumeStock() {
        Long itemId = saveItem("Dragon Tail", 10, new BigDecimal("2.00"));

        Long productId = saveProduct(itemId, "Dragon Tail Potion");

        var request = new SaleController.CreateSaleRequest(
                List.of(new SaleController.CreateSaleItemRequest(productId, 3))
                );

        ResponseEntity<SaleController.CreateSaleResponse> response =
                restTemplate.postForEntity(
                        "/sale",
                        request,
                        SaleController.CreateSaleResponse.class
                );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().saleId());

        Long saleId = response.getBody().saleId();

        assertTrue(sales.existsById(saleId));
        // Available stock includes committed movements not yet consolidated into item_inventory.
        assertEquals(7, inventories.findByActiveTrueAndItemId(itemId).orElseThrow().getCurrentStock()
                + transactions.findTotalUnprocessedStocksByItemId(itemId));

        Integer movementQuantity =
                jdbc.queryForObject(
                        """
                        SELECT quantity
                        FROM inventory_transaction
                        WHERE source_id = ?
                          AND type = 'SALE'
                          AND item_id = ?
                        """,
                        Integer.class,
                        saleId,
                        itemId
                );



        assertEquals(3, movementQuantity);


        Integer quantity =
                jdbc.queryForObject(
                        """
                        SELECT quantity
                        FROM sale_item
                        WHERE sale_id = ?
                          AND product_id = ?
                        """,
                        Integer.class,
                        saleId,
                        productId
                );

        assertEquals(3, quantity);
    }



    private Long saveItem(String name, int initialStock, BigDecimal cost) {
        Long id = items.saveAndFlush(new ItemEntity(name, "Description", true))
                .getId();

        inventories.saveAndFlush(
                new ItemInventoryEntity(
                        null,
                        id,
                        cost,
                        0,
                        initialStock,
                        true
                )
        );

        return id;
    }


    private Long saveProduct(Long itemId, String name) {
        return products.saveAndFlush(
                new ProductEntity(
                        null,
                        itemId,
                        ProductType.SIMPLE_PRODUCT,
                        name,
                        new BigDecimal("10.00"),
                        true
                )
        ).getId();
    }

}

