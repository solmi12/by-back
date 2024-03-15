    package com.example.camp.dto;

    import com.example.camp.entity.Category;
    import com.fasterxml.jackson.annotation.JsonFormat;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.Column;
    import jakarta.persistence.Transient;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDate;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ToolDTO {
        private Long toolId;
        private String toolName;
        private String description;
        private Integer availableQuantity;
        private CategoryDTO category;
        private String imageData;
        private Double price;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate promotionStartDate;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate promotionEndDate;
        @Column(name = "discounted_price")
        private Double discountedPrice;
        private Double totalPrice;
    }
