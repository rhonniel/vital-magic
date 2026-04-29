package com.lps.vitalMagic.shake.infrastructure.persistance.repository.impl;

import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.repository.ShakeRepository;
import com.lps.vitalMagic.shake.infrastructure.persistance.entity.ShakeEntity;
import com.lps.vitalMagic.shake.infrastructure.persistance.mapper.ShakeMapper;
import com.lps.vitalMagic.shake.infrastructure.persistance.repository.ShakeEntityJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
}
