package com.monkcommerce.coupon.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    private String type;


    @Column(columnDefinition = "TEXT")
    private String metadata;

    private Boolean active = true;

    private LocalDateTime expirationDate;
}

