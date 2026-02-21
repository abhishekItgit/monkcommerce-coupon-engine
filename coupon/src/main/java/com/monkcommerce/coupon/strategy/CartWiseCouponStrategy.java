package com.monkcommerce.coupon.strategy;


import com.monkcommerce.coupon.dto.*;
import com.monkcommerce.coupon.model.Coupon;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import java.util.Map;

@Component("CART_WISE")
public class CartWiseCouponStrategy implements CouponStrategy {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ApplicableCouponResponse checkApplicable(Coupon coupon, CartRequest cart) {

        try {

            Map<String, Object> meta =
                    mapper.readValue(coupon.getMetadata(), Map.class);

            double threshold = Double.parseDouble(meta.get("threshold").toString());
            double discountPercent =
                    Double.parseDouble(meta.get("discountPercent").toString());

            double total = cart.getItems().stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();

            if (total > threshold) {

                double discount = total * discountPercent / 100;

                return new ApplicableCouponResponse(
                        coupon.getCouponId(),
                        coupon.getType(),
                        discount
                );
            }

        } catch (Exception e) {
            throw new RuntimeException("Invalid metadata for CART_WISE coupon");
        }

        return null;
    }

    @Override
    public ApplyCouponResponse applyCoupon(Coupon coupon, CartRequest cart) {

        ApplicableCouponResponse applicable = checkApplicable(coupon, cart);

        if (applicable == null) return null;

        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        double discount = applicable.getDiscount();

        ApplyCouponResponse response = new ApplyCouponResponse();

        response.setItems(cart.getItems());
        response.setTotalPrice(total);
        response.setTotalDiscount(discount);
        response.setFinalPrice(total - discount);

        return response;
    }
}
