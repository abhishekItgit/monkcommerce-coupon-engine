package com.monkcommerce.coupon.strategy;

import com.monkcommerce.coupon.dto.*;
import com.monkcommerce.coupon.model.Coupon;

public interface CouponStrategy {
    ApplicableCouponResponse checkApplicable(Coupon coupon, CartRequest cart);
    ApplyCouponResponse applyCoupon(Coupon coupon, CartRequest cart);
}

