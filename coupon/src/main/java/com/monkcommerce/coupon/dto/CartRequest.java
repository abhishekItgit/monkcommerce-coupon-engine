package com.monkcommerce.coupon.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartRequest {

    private List<CartItem> items;
}
