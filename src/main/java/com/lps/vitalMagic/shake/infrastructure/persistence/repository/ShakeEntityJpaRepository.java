package com.lps.vitalMagic.shake.infrastructure.persistence.repository;

import com.lps.vitalMagic.shake.infrastructure.persistence.entity.ShakeEntity;
import com.lps.vitalMagic.shake.infrastructure.persistence.projection.ShakeAttributeProjection;
import com.lps.vitalMagic.shake.infrastructure.persistence.projection.ShakeIngredientProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ShakeEntityJpaRepository extends JpaRepository<ShakeEntity,Long>, JpaSpecificationExecutor<ShakeEntity> {
    @Query("""
    select si.shakeId as shakeId,
           i.id as itemId,
           i.name as itemName,
           si.quantity as quantity
    from ShakeIngredientEntity si
    join ItemEntity i on i.id = si.itemId
    where si.shakeId in :shakeIds
    """)
    List<ShakeIngredientProjection> findIngredientsByShakeIds(
            Collection<Long> shakeIds
    );

    @NativeQuery("""
          select
          si.shake_id as shakeId,
          a.id as attributeId,
          a.name as attributeName,
          sum(ia.value * si.quantity) as total
          from shake_ingredient si
          join item_attribute ia on ia.item_id = si.item_id
          join attribute a on a.id = ia.attribute_id
          where si.shake_id in (:shakeIds)
          group by si.shake_id, a.id, a.name
    """)
    List<ShakeAttributeProjection> findAttributeByShakeIds(
            Collection<Long> shakeIds
    );

}
