package com.example.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import com.verve.test.dto.PromotionResponse;
import com.verve.test.entity.Promotion;
import com.verve.test.repository.PromotionRepository;
import com.verve.test.service.PromotionServiceImpl;

@ExtendWith(MockitoExtension.class)
class PromotionServiceImplTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job job;

    @InjectMocks
    private PromotionServiceImpl promotionService;

    private static final DateTimeFormatter RESPONSE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void setUp() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        // Mock jobLauncher.run() to return a dummy JobExecution with a completed status
        JobExecution jobExecution = new JobExecution(new JobInstance(1L, "importPromotionJob"), new JobParameters());
        jobExecution.setStatus(BatchStatus.COMPLETED);
        when(jobLauncher.run(any(Job.class), any(JobParameters.class))).thenReturn(jobExecution);

    }

    @Test
    void testGetPromotionById() {
        String testId = "TEST123";
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);
        Promotion mockPromotion = new Promotion(testId, 50.0, expirationDate);

        when(promotionRepository.findById(testId.toUpperCase())).thenReturn(Optional.of(mockPromotion));

        Optional<PromotionResponse> result = promotionService.getPromotionById(testId);
        assertTrue(result.isPresent());
        assertEquals(testId, result.get().getId());
        assertEquals(50.0, result.get().getPrice());
        assertEquals(expirationDate.format(RESPONSE_DATE_FORMATTER), result.get().getExpirationDate());
    }

    @Test
    void testGetPromotionById_NotFound() {
        // Mock promotionRepository.findById() to return an empty Optional
        String testId = "NOTFOUND";

        when(promotionRepository.findById(testId.toUpperCase())).thenReturn(Optional.empty());

        Optional<PromotionResponse> result = promotionService.getPromotionById(testId);
        assertFalse(result.isPresent());
    }

    @Test
    void testLoadAndSaveData() throws Exception {
        // Test the scheduled batch job execution
        BatchStatus batchStatus = promotionService.loadAndSaveData();
        assertEquals(BatchStatus.COMPLETED, batchStatus);
    }
}
