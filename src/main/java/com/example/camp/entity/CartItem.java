package com.example.camp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER) // Change the fetch type to EAGER
    @JsonIgnore
    @JoinColumn(name = "tool_id")
    private Tools tool;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JsonIgnore
    private UserCartItems userCartItems;

    @Column(name = "total_price_cart")
    private Double totalPriceCart;
}
