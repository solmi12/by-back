package com.example.camp.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tools {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long toolId;
    @NonNull
    private String toolName;
    private String description;



   @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Double price;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image;

    private Integer availableQuantity;
    @Column(name = "discounted_price")
    private Double discountedPrice;
    private Double totalPrice;

    @OneToMany(mappedBy = "tool")
    private List<CartItem> cartItems = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate promotionEndDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate promotionStartDate;
}
