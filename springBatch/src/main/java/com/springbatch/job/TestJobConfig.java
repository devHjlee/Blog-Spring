package com.springbatch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Slf4j
@Configuration
public class TestJobConfig {
    @Bean
    public Job testJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("testJob", jobRepository)
                .start(testJobStepOne(null,jobRepository, transactionManager))
                .next(testJobStepTwo(jobRepository, transactionManager))
                .build();
    }

    @Bean
    @JobScope
    public Step testJobStepOne(@Value("#{jobParameters[requestDate]}") String requestDate, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testJobStepOne", jobRepository)
                .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
                    log.info(">>>>> This is testJobStepOne {}",requestDate);

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step testJobStepTwo(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("testJobStepTwo", jobRepository)
                .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
                    log.info(">>>>> testJobStepTwo");

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}