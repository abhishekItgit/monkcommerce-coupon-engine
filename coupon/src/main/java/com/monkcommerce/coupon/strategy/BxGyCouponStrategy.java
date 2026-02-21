package com.monkcommerce.coupon.strategy;

import com.monkcommerce.coupon.dto.*;
import com.monkcommerce.coupon.model.Coupon;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Component("BXGY")
public class BxGyCouponStrategy implements CouponStrategy {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ApplicableCouponResponse checkApplicable(Coupon coupon, CartRequest cart) {

        try {

            Map<String, Object> meta =
                    mapper.readValue(coupon.getMetadata(), Map.class);

            int buyQty = Integer.parseInt(meta.get("buyQty").toString());
            int getQty = Integer.parseInt(meta.get("getQty").toString());
            int repetitionLimit =
                    Integer.parseInt(meta.get("repetitionLimit").toString());

            int totalItems = cart.getItems().stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();

            int repetitions =
                    Math.min(totalItems / buyQty, repetitionLimit);

            if (repetitions <= 0) return null;

            double cheapestPrice = cart.getItems().stream()
                    .mapToDouble(CartItem::getPrice)
                    .min()
                    .orElse(0);

            double discount = repetitions * getQty * cheapestPrice;

            return new ApplicableCouponResponse(
                    coupon.getCouponId(),
                    coupon.getType(),
                    discount
            );

        } catch (Exception e) {
            throw new RuntimeException("Invalid metadata for BXGY coupon");
        }
    }

    @Override
    public ApplyCouponResponse applyCoupon(Coupon coupon, CartRequest cart) {

        ApplicableCouponResponse applicable =
                checkApplicable(coupon, cart);

        if (applicable == null) return null;

        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        ApplyCouponResponse response = new ApplyCouponResponse();

        response.setItems(cart.getItems());
        response.setTotalPrice(total);
        response.setTotalDiscount(applicable.getDiscount());
        response.setFinalPrice(total - applicable.getDiscount());

        return response;
    }
}
