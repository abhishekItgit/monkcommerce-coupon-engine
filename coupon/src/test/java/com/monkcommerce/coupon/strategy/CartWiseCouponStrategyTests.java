package com.monkcommerce.coupon.strategy;

import com.monkcommerce.coupon.dto.CartItem;
import com.monkcommerce.coupon.dto.CartRequest;
import com.monkcommerce.coupon.dto.ApplicableCouponResponse;
import com.monkcommerce.coupon.model.Coupon;
import com.monkcommerce.coupon.model.CouponType;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CartWiseCouponStrategyTests {

    @Test
    void shouldApplyDiscount_whenCartTotalExceedsThreshold() {

        // Arrange
        CartWiseCouponStrategy strategy = new CartWiseCouponStrategy();

        Coupon coupon = new Coupon();
        coupon.setCouponId(1L);
        coupon.setType(CouponType.CART_WISE);
        coupon.setMetadata("{\"threshold\":100,\"discountPercent\":10}");

        CartItem item = new CartItem();
        item.setProductId(1L);
        item.setQuantity(2);
        item.setPrice(60); // total = 120

        CartRequest cart = new CartRequest();
        cart.setItems(List.of(item));

        // Act
        ApplicableCouponResponse response =
                strategy.checkApplicable(coupon, cart);

        // Assert
        assertNotNull(response);
        assertEquals(CouponType.CART_WISE, response.getType());
        assertTrue(response.getDiscount() > 0);
    }

    @Test
    void shouldReturnNull_whenThresholdNotMet() {

        CartWiseCouponStrategy strategy = new CartWiseCouponStrategy();

        Coupon coupon = new Coupon();
        coupon.setCouponId(1L);
        coupon.setType(CouponType.CART_WISE);
        coupon.setMetadata("{\"threshold\":200,\"discountPercent\":10}");

        CartItem item = new CartItem();
        item.setProductId(1L);
        item.setQuantity(1);
        item.setPrice(50); // total = 50

        CartRequest cart = new CartRequest();
        cart.setItems(List.of(item));

        ApplicableCouponResponse response =
                strategy.checkApplicable(coupon, cart);

        assertNull(response);
    }
}