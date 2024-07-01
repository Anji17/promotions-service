package com.example.test.controller;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.verve.test.controller.PromotionController;
import com.verve.test.dto.PromotionResponse;
import com.verve.test.service.PromotionService;

//@ExtendWith(MockitoExtension.class)
public class PromotionControllerTest {

    @Mock
    private PromotionService yourService;

    @InjectMocks
    private PromotionController yourController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(yourController).build();
    }

    @Test
    public void testGetPromotionById() throws Exception {
        String id = "172FFC14-D229-4C93-B06B-F48B8C095512";
        PromotionResponse mockPromotion = new PromotionResponse(id, 9.68, "2022-06-04T06:01:20");

//        Mockito.when(yourService.getPromotionById(id)).thenReturn(mockPromotion);

        mockMvc.perform(MockMvcRequestBuilders.get("/promotions/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(9.68))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiration_date").value("2022-06-04T06:01:20"));
    }
}
