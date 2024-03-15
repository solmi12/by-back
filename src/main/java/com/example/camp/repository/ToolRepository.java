package com.example.camp.repository;


import com.example.camp.dto.ToolDTO;
import com.example.camp.entity.Tools;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tools, Long> {
    List<Tools> findByCategoryCategoryId(Long categoryId);

    @Modifying
    @Query("UPDATE Tools t SET t.discountedPrice = :discountedPrice WHERE t.toolId = :toolId")
    void updateDiscountedPrice(@Param("toolId") Long toolId, @Param("discountedPrice") Double discountedPrice);
    List<Tools> findByToolNameContaining(String toolName);

    List<Tools> findByDiscountedPriceIsNotNull();
    List<Tools> findByCategoryCategoryName(String categoryName);

}
