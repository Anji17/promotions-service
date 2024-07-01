package com.verve.test.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.verve.test.dto.PromotionResponse;
import com.verve.test.service.PromotionService;

@RestController
@RequestMapping("/promotions")
public class PromotionController {
    
    @Autowired
    private PromotionService promotionService;

    @GetMapping("/{id}")
    public ResponseEntity<PromotionResponse> getPromotion(@PathVariable String id) {
    	Optional<PromotionResponse> promotion = promotionService.getPromotionById(id);
    	return promotion.map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
