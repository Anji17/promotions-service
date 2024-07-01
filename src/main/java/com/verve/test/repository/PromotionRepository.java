package com.verve.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.verve.test.entity.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, String> {

}
