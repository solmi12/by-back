    package com.example.camp.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDate;
    import java.util.List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    public class Promotion {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long promotionId;

        private String name;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
        private double discountPercentage;


    }