package com.monkcommerce.coupon.dto;

import com.monkcommerce.coupon.model.CouponType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicableCouponResponse {

    private Long couponId;
    private CouponType type;
    private double discount;
}
