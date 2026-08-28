package com.lps.vitalMagic.sale.infrastructure;

import com.lps.vitalMagic.common.pagination.PageResult;
import com.lps.vitalMagic.common.pagination.Pagination;
import com.lps.vitalMagic.config.MySqlDataJpaTest;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.product.infrastructure.persistance.entity.ProductEntity;
import com.lps.vitalMagic.product.infrastructure.persistance.repository.ProductEntityJpaRepository;
import com.lps.vitalMagic.sales.application.query.SearchSaleQuery;
import com.lps.vitalMagic.sales.application.view.SaleView;
import com.lps.vitalMagic.sales.domain.model.entity.Sale;
import com.lps.vitalMagic.sales.domain.model.entity.SaleItem;
import com.lps.vitalMagic.sales.infrastructure.persistence.repository.impl.JpaSaleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaSaleRepository.class)
public class SaleJpaRepositoryTest extends MySqlDataJpaTest {


    @Autowired
    private JpaSaleRepository repository;

    @Autowired
    private ProductEntityJpaRepository productRepository;

    @Test
    void shouldSaveAndFindSale() {
        ProductEntity product = saveProduct(
                "Health Potion",
                new BigDecimal("10.00")
        );

        Sale sale = createSale(
                product,
                2,
                LocalDateTime.of(2026, 8, 10, 10, 0)
        );

        Sale savedSale = repository.save(sale);

        Sale result = repository.findById(savedSale.getId())
                .orElseThrow();

        assertNotNull(result.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(
                product.getId(),
                result.getItems()
                        .get(0)
                        .getProductSnapshot()
                        .getProductId()
        );
        assertEquals(
                0,
                new BigDecimal("20.00")
                        .compareTo(result.getTotalAmount())
        );
    }

    @Test
    void shouldSearchSalesByDateAndProduct() {
        ProductEntity selectedProduct = saveProduct(
                "Health Potion",
                new BigDecimal("10.00")
        );

        ProductEntity excludedProduct = saveProduct(
                "Magic Potion",
                new BigDecimal("15.00")
        );

        Sale selectedSale = repository.save(
                createSale(
                        selectedProduct,
                        2,
                        LocalDateTime.of(2026, 8, 10, 10, 0)
                )
        );

        repository.save(
                createSale(
                        selectedProduct,
                        1,
                        LocalDateTime.of(2026, 8, 20, 10, 0)
                )
        );

        repository.save(
                createSale(
                        excludedProduct,
                        1,
                        LocalDateTime.of(2026, 8, 11, 10, 0)
                )
        );

        SearchSaleQuery query = new SearchSaleQuery(
                LocalDate.of(2026, 8, 9),
                LocalDate.of(2026, 8, 12),
                selectedProduct.getId(),
                new Pagination(0, 10)
        );

        PageResult<SaleView> result = repository.search(query);

        assertEquals(1, result.content().size());
        assertEquals(
                selectedSale.getId(),
                result.content().get(0).id()
        );
    }

    @Test
    void shouldReturnSalesOrderedAndPaginated() {
        ProductEntity product = saveProduct(
                "Health Potion",
                new BigDecimal("10.00")
        );

        Sale oldestSale = repository.save(
                createSale(
                        product,
                        1,
                        LocalDateTime.of(2026, 8, 8, 10, 0)
                )
        );

        Sale middleSale = repository.save(
                createSale(
                        product,
                        1,
                        LocalDateTime.of(2026, 8, 10, 10, 0)
                )
        );

        Sale newestSale = repository.save(
                createSale(
                        product,
                        1,
                        LocalDateTime.of(2026, 8, 12, 10, 0)
                )
        );

        SearchSaleQuery query = new SearchSaleQuery(
                null,
                null,
                null,
                new Pagination(0, 2)
        );

        PageResult<SaleView> result = repository.search(query);

        assertEquals(2, result.content().size());
        assertEquals(
                newestSale.getId(),
                result.content().get(0).id()
        );
        assertEquals(
                middleSale.getId(),
                result.content().get(1).id()
        );
        assertEquals(3, result.totalElements());
        assertEquals(2, result.totalPages());
    }

    private ProductEntity saveProduct(
            String name,
            BigDecimal price
    ) {
        return productRepository.saveAndFlush(
                new ProductEntity(
                        null,
                        1L,
                        ProductType.SIMPLE_PRODUCT,
                        name,
                        price,
                        true
                )
        );
    }

    private Sale createSale(
            ProductEntity product,
            int quantity,
            LocalDateTime createdAt
    ) {
        BigDecimal subtotal = product.getPrice()
                .multiply(BigDecimal.valueOf(quantity));

        SaleItem item = SaleItem.from(
                null,
                product.getId(),
                product.getName(),
                product.getPrice(),
                quantity,
                subtotal
        );

        return Sale.from(
                null,
                List.of(item),
                subtotal,
                createdAt
        );
    }

}
