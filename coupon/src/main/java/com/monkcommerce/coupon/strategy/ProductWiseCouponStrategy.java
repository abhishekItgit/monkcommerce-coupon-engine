package com.monkcommerce.coupon.strategy;


import com.monkcommerce.coupon.dto.*;
import com.monkcommerce.coupon.model.Coupon;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Component("PRODUCT_WISE")
public class ProductWiseCouponStrategy implements CouponStrategy {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ApplicableCouponResponse checkApplicable(Coupon coupon, CartRequest cart) {

        try {

            Map<String, Object> meta =
                    mapper.readValue(coupon.getMetadata(), Map.class);

            Long productId =
                    Long.parseLong(meta.get("productId").toString());

            double discountPercent =
                    Double.parseDouble(meta.get("discountPercent").toString());

            double discount = 0;

            for (CartItem item : cart.getItems()) {

                if (item.getProductId().equals(productId)) {

                    discount += item.getPrice()
                            * item.getQuantity()
                            * discountPercent / 100;
                }
            }

            if (discount > 0) {
                return new ApplicableCouponResponse(
                        coupon.getCouponId(),
                        coupon.getType(),
                        discount
                );
            }

        } catch (Exception e) {
            throw new RuntimeException("Invalid metadata for PRODUCT_WISE coupon");
        }

        return null;
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
