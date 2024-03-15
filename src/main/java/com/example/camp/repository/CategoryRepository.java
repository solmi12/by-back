package com.example.camp.repository;


import com.example.camp.dto.CategoryDTO;
import com.example.camp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // You can add custom query methods if needed
}