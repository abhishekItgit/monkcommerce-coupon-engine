package com.monkcommerce.coupon.dto;

import lombok.Data;
import java.util.List;

@Data
public class ApplyCouponResponse {

    private List<CartItem> items;
    private double totalPrice;
    private double totalDiscount;
    private double finalPrice;
}
