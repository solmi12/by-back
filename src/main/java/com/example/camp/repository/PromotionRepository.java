package com.example.camp.repository;

import com.example.camp.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}