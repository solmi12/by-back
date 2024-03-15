package com.example.camp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    private Long cartId;
    private int quantity;
    private Double totalPriceCart;
    private ToolDTO tool;
    private UserDTO user;
}
