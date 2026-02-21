package com.monkcommerce.coupon.factory;

import com.monkcommerce.coupon.strategy.CouponStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CouponStrategyFactory {

    private final Map<String, CouponStrategy> strategyMap;

    public CouponStrategyFactory(Map<String, CouponStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public CouponStrategy getStrategy(String type) {
        return strategyMap.get(type);
    }
}
