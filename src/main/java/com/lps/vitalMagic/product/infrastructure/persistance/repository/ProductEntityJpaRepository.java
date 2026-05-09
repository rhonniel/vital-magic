package com.lps.vitalMagic.product.infrastructure.persistance.repository;

import com.lps.vitalMagic.product.infrastructure.persistance.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductEntityJpaRepository extends JpaRepository<ProductEntity,Long> {
    List<ProductEntity> findByActiveTrue();
}
