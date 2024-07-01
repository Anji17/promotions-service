package com.example.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.verve.test.controller.PromotionController;
import com.verve.test.dto.PromotionResponse;
import com.verve.test.service.PromotionService;

@ExtendWith(MockitoExtension.class)
class PromotionControllerTest {

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private PromotionController promotionController;

    @Test
    void testGetPromotionFound() throws Exception {
        PromotionResponse promotionResponse = new PromotionResponse("172FFC14-D229-4C93-B06B-F48B8C095512", 9.68, "2022-06-04 06:01:20");
        when(promotionService.getPromotionById(anyString())).thenReturn(Optional.of(promotionResponse));
        assertEquals(HttpStatus.OK, promotionController.getPromotion("172FFC14-D229-4C93-B06B-F48B8C095512").getStatusCode());
        assertEquals(promotionResponse, promotionController.getPromotion("172FFC14-D229-4C93-B06B-F48B8C095512").getBody());
    }

    @Test
    void testGetPromotionNotFound() throws Exception {
        when(promotionService.getPromotionById(anyString())).thenReturn(Optional.empty());
        assertEquals(HttpStatus.NOT_FOUND, promotionController.getPromotion("172FFC14-D229-4C93-B06B").getStatusCode());
    }
}
