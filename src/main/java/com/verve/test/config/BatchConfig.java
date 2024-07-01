package com.verve.test.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.verve.test.dto.PromotionItemProcessor;
import com.verve.test.dto.PromotionReader;
import com.verve.test.entity.Promotion;

@Configuration
public class BatchConfig extends DefaultBatchConfiguration {

	@Value("${csv.file.path}")
	private String inputFilePath;
	
	@Value("${chunk.size}")
	private int chunkSize;

	@Bean
	public FlatFileItemReader<PromotionReader> flatFileItemReader() {
		return new FlatFileItemReaderBuilder<PromotionReader>().name("promotionReader")
				.resource(new PathResource(inputFilePath)).delimited().strict(false)
				.names("id", "price", "expirationDate").targetType(PromotionReader.class).build();
	}

	@Bean
	public PromotionItemProcessor processor() {
		return new PromotionItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Promotion> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Promotion>()
				.sql("INSERT INTO PROMOTION (id, price, expiration_date) VALUES (:id, :price, :expirationDate)")
				.dataSource(dataSource).beanMapped().build();
	}

	@Bean
	public Job importPromotionJob(JobRepository jobRepository, Step step1, Step step2) {
		return new JobBuilder("promotionJob", jobRepository).preventRestart().start(step1).next(step2).build();
	}

	@Bean
	public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			FlatFileItemReader<PromotionReader> reader, PromotionItemProcessor processor,
			JdbcBatchItemWriter<Promotion> writer) {
		return new StepBuilder("step2", jobRepository).<PromotionReader, Promotion>chunk(chunkSize, transactionManager)
				.reader(reader).processor(processor).writer(writer).build();
	}

	@Bean
	public Step step1(JobRepository jobRepository, DataSource dataSource, Tasklet truncateTableTasklet,
			PlatformTransactionManager transactionManager) {
		return new StepBuilder("step1", jobRepository).tasklet(truncateTableTasklet, transactionManager).build();
	}

	@Bean
	public Tasklet truncateTableTasklet(DataSource dataSource) {
		return (contribution, chunkContext) -> {
			new JdbcTemplate(dataSource).execute("TRUNCATE TABLE PROMOTION");
			return RepeatStatus.FINISHED;
		};
	}

}
