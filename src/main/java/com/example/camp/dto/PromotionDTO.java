package com.example.camp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDTO {
    private Long promotionId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private double discountPercentage;
}