package com.verve.test.service;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.verve.test.dto.PromotionResponse;
import com.verve.test.entity.Promotion;
import com.verve.test.repository.PromotionRepository;

@Service
public class PromotionServiceImpl implements PromotionService {
	private static final Logger logger = LoggerFactory.getLogger(PromotionServiceImpl.class);
    
    @Autowired
    private PromotionRepository promotionRepository;
    
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;
    
    private static final DateTimeFormatter RESPONSE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    
    public Optional<PromotionResponse> getPromotionById(String id) {
    	Optional<Promotion> promotion = promotionRepository.findById(id.toUpperCase());
    	PromotionResponse response = null;
        if (promotion.isPresent()) {
        	response = convertToDto(promotion.get());
            logger.debug("Promotion found for ID: {}", id);
        } else {
            logger.debug("Promotion not found for ID: {}", id);
        }
        return Optional.ofNullable(response);
    }
    
    private PromotionResponse convertToDto(Promotion promotion) {
        return new PromotionResponse(
            promotion.getId(),
            promotion.getPrice(),
            promotion.getExpirationDate().format(RESPONSE_DATE_FORMATTER)
        );
    }
    
    @Scheduled(fixedRate = 1800000)
    public BatchStatus loadAndSaveData() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("timestamp", Calendar.getInstance().getTime())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        return jobExecution.getStatus();
    }
    
}
