package com.lps.vitalMagic.shake.infrastructure.persistence.repository.impl;

import com.lps.vitalMagic.common.presentation.pagination.PageResult;
import com.lps.vitalMagic.shake.application.query.SearchShakeQuery;
import com.lps.vitalMagic.shake.application.view.ShakeView;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.repository.ShakeRepository;
import com.lps.vitalMagic.shake.infrastructure.persistence.entity.ShakeEntity;
import com.lps.vitalMagic.shake.infrastructure.persistence.mapper.ShakeMapper;
import com.lps.vitalMagic.shake.infrastructure.persistence.projection.ShakeAttributeProjection;
import com.lps.vitalMagic.shake.infrastructure.persistence.projection.ShakeIngredientProjection;
import com.lps.vitalMagic.shake.infrastructure.persistence.repository.ShakeEntityJpaRepository;
import com.lps.vitalMagic.shake.infrastructure.persistence.specification.ShakeSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JpaShakeRepository implements ShakeRepository {

    private final ShakeEntityJpaRepository jpaRepository;

    public JpaShakeRepository(ShakeEntityJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Shake> findById(Long id) {
        return jpaRepository.findById(id).map(ShakeMapper::toDomain);
    }

    @Override
    public Shake save(Shake shake) {
        ShakeEntity entity= jpaRepository.save(ShakeMapper.toEntity(shake));
        return ShakeMapper.toDomain(entity);
    }

    @Override
    public PageResult<ShakeView> searchAvailableShakes(SearchShakeQuery query) {


        Pageable pageable = PageRequest.of(
                query.pagination().page(),
                query.pagination().size()
        );

        Page<ShakeEntity> page = jpaRepository.findAll(
                ShakeSpecifications.withFilters(query),
                pageable
        );


        Set<Long> shakeIds = page.getContent().stream()
                .map(ShakeEntity::getId)
                .collect(Collectors.toSet());

        List<ShakeAttributeProjection> attributeProjections= jpaRepository.findAttributeByShakeIds(shakeIds);
        List<ShakeIngredientProjection> ingredientProjections=jpaRepository.findIngredientsByShakeIds(shakeIds);




        return ShakeMapper.toPageResult(page,attributeProjections,ingredientProjections);

    }
}
