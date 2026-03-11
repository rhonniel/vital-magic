package com.lps.vitalMagic.product.domain.repository;

import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    Product save(Product product);
    List<Product> findAllActiveProducts();
    List<Product> findProductsByType(ProductType type);
}
