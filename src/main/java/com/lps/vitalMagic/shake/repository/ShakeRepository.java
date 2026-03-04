package com.lps.vitalMagic.shake.repository;

import com.lps.vitalMagic.shake.model.entity.Shake;
import com.lps.vitalMagic.shake.model.enums.ShakeType;

import java.util.List;
import java.util.Optional;

public interface ShakeRepository {
    Optional<Shake> findById(Long id);
    Shake save(Shake shake);
    List<Shake> findAllAvailableShakes();
    List<Shake> findShakeByType(ShakeType type);

}
