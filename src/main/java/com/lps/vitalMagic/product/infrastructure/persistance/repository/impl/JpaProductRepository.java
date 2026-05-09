package com.lps.vitalMagic.product.infrastructure.persistance.repository.impl;

import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.repository.ProductRepository;
import com.lps.vitalMagic.product.infrastructure.persistance.entity.ProductEntity;
import com.lps.vitalMagic.product.infrastructure.persistance.mapper.ProductMapper;
import com.lps.vitalMagic.product.infrastructure.persistance.repository.ProductEntityJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaProductRepository implements ProductRepository {

    private final ProductEntityJpaRepository jpaRepository;

    public JpaProductRepository(ProductEntityJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id).map(ProductMapper::toDomain);
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity=ProductMapper.toEntity(product);
        return ProductMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public List<Product> findAllActiveProducts() {
        return jpaRepository.findByActiveTrue().stream().map(ProductMapper::toDomain).toList();
    }
}
