package com.lps.vitalMagic.product.domain.model.entity;

import com.lps.vitalMagic.product.domain.exception.InvalidProductException;
import com.lps.vitalMagic.product.domain.model.data.IngredientCost;
import com.lps.vitalMagic.product.domain.model.data.ShakeProductData;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.model.entity.ShakeIngredient;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

@Getter
public class Product {

    private Long id;


    private Long referenceNo;


    private ProductType productType;


    private String name;


    private BigDecimal price;

    private boolean active;

    private Product() {
    }

    private Product(Long referenceNo, ProductType productType, String name, BigDecimal price, boolean active) {
        if(price==null || price.compareTo(BigDecimal.ZERO)<=0){
            throw new InvalidProductException("Price should be more than Zero");
        }

        this.referenceNo = Objects.requireNonNull(referenceNo);
        this.productType = Objects.requireNonNull(productType);
        this.name = Objects.requireNonNull(name);
        this.price = price;
        this.active = active;
    }

    public static Product createShakeProduct(ShakeProductData data) {
        Objects.requireNonNull(data);

        BigDecimal totalCost = data.ingredients().stream()
                .map(IngredientCost::totalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal price = calculatePrice(totalCost);

        if (price.compareTo(totalCost) <= 0) {
            throw new InvalidProductException(
                    "Product price should be greater than total cost"
            );
        }

        return new Product(
                data.referenceNo(),
                ProductType.SHAKE,
                data.name(),
                price,
                true
        );
    }

    /*Simple price using fixed profit margin, MVP version*/
    private static BigDecimal calculatePrice(BigDecimal productTotalCost) {
        return productTotalCost.multiply(BigDecimal.valueOf(1.3)).setScale(2, RoundingMode.HALF_UP);
    }

    public static Product from(Long id,Long referenceNo, ProductType productType,
                               String name, BigDecimal price, boolean active){
        Product product= new Product();
        product.id=id;
        product.productType=productType;
        product.referenceNo=referenceNo;
        product.name=name;
        product.price=price;
        product.active=active;

        return product;

    }


}
