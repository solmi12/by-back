package com.example.camp.repository;

import com.example.camp.entity.CartItem;

import com.example.camp.entity.UserCartItems;
import com.example.camp.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // You can add custom query methods if needed
    List<CartItem> findByUserCartItems_User(Users user);
    void deleteByUserCartItems(UserCartItems userCartItems);
}