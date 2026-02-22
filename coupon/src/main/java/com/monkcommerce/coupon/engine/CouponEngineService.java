package com.monkcommerce.coupon.engine;

import com.monkcommerce.coupon.dto.*;
import com.monkcommerce.coupon.factory.CouponStrategyFactory;
import com.monkcommerce.coupon.model.Coupon;
import com.monkcommerce.coupon.repository.CouponRepository;
import com.monkcommerce.coupon.strategy.CouponStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CouponEngineService {

    private final CouponRepository couponRepository;
    private final CouponStrategyFactory strategyFactory;

    public CouponEngineService(
            CouponRepository couponRepository,
            CouponStrategyFactory strategyFactory) {

        this.couponRepository = couponRepository;
        this.strategyFactory = strategyFactory;
    }

    // GET all applicable coupons
    public List<ApplicableCouponResponse> getApplicableCoupons(CartRequest cart) {

        List<Coupon> coupons = couponRepository.findAll();

        List<ApplicableCouponResponse> result = new ArrayList<>();

        for (Coupon coupon : coupons) {

            if (coupon.getActive() == null || !coupon.getActive()) continue;

            if (coupon.getExpirationDate() != null &&
                    coupon.getExpirationDate().isBefore(java.time.LocalDateTime.now()))
                continue;

            CouponStrategy strategy =
                    strategyFactory.getStrategy(coupon.getType());

            if (strategy == null) continue;

            ApplicableCouponResponse response =
                    strategy.checkApplicable(coupon, cart);

            if (response != null) {
                result.add(response);
            }
        }

        return result;
    }

    // APPLY specific coupon
    public ApplyCouponResponse applyCoupon(Long id, CartRequest cart) {

        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        CouponStrategy strategy =
                strategyFactory.getStrategy(coupon.getType());

        if (strategy == null)
            throw new RuntimeException("Strategy not found");

        return strategy.applyCoupon(coupon, cart);
    }
}
