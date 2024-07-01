package com.verve.test.service;

import java.util.Optional;

import com.verve.test.dto.PromotionResponse;

public interface PromotionService {

	Optional<PromotionResponse> getPromotionById(String id);

}
