package com.example.camp.dto;

import com.example.camp.entity.CartItem;
import com.example.camp.entity.UserCartItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class UserCartItemsResponseDTO {
    private UserCartItems userCartItems;
    private List<CartItem> cartItems;
}