package com.monkcommerce.coupon.controller;

import com.monkcommerce.coupon.dto.*;
import com.monkcommerce.coupon.engine.CouponEngineService;
import com.monkcommerce.coupon.model.Coupon;
import com.monkcommerce.coupon.repository.CouponRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponRepository couponRepository;
    private final CouponEngineService engineService;

    public CouponController(
            CouponRepository couponRepository,
            CouponEngineService engineService) {

        this.couponRepository = couponRepository;
        this.engineService = engineService;
    }

    // CREATE COUPON
    @PostMapping
    public ResponseEntity<ApiResponse<Coupon>> createCoupon(
            @RequestBody Coupon coupon) {

        Coupon saved = couponRepository.save(coupon);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupon created successfully",
                        saved
                )
        );
    }

    //  GET ALL COUPONS
    @GetMapping
    public ResponseEntity<ApiResponse<List<Coupon>>> getAllCoupons() {

        List<Coupon> coupons = couponRepository.findAll();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupons fetched successfully",
                        coupons
                )
        );
    }

    // GET COUPON BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Coupon>> getCoupon(
            @PathVariable Long id) {

        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupon fetched successfully",
                        coupon
                )
        );
    }

    //  UPDATE COUPON
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Coupon>> updateCoupon(
            @PathVariable Long id,
            @RequestBody Coupon updated) {

        Coupon existing = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        existing.setType(updated.getType());
        existing.setMetadata(updated.getMetadata());
        existing.setActive(updated.getActive());
        existing.setExpirationDate(updated.getExpirationDate());

        Coupon saved = couponRepository.save(existing);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupon updated successfully",
                        saved
                )
        );
    }

    // DELETE COUPON
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(
            @PathVariable Long id) {

        couponRepository.deleteById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupon deleted successfully",
                        null
                )
        );
    }

    // GET APPLICABLE COUPONS
    @PostMapping("/applicable-coupons")
    public ResponseEntity<ApiResponse<List<ApplicableCouponResponse>>> applicableCoupons(
            @RequestBody CartRequest cart) {

        List<ApplicableCouponResponse> result =
                engineService.getApplicableCoupons(cart);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Applicable coupons fetched successfully",
                        result
                )
        );
    }

    //  APPLY SPECIFIC COUPON
    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<ApiResponse<ApplyCouponResponse>> applyCoupon(
            @PathVariable Long id,
            @RequestBody CartRequest cart) {

        ApplyCouponResponse response =
                engineService.applyCoupon(id, cart);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Coupon applied successfully",
                        response
                )
        );
    }
}
