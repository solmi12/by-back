package com.example.camp.repository;

import com.example.camp.entity.UserCartItems;
import com.example.camp.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCartItemsRepository extends JpaRepository<UserCartItems, Long> {
    Optional<UserCartItems> findByUser(Users user);
}
