package com.springbatch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
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
public class JobFlowConfig {

    @Bean
    public Job jobFlow(JobRepository jobRepository, Step stepOne, Step stepTwo, Step stepThree) {
        return new JobBuilder("jobFlow", jobRepository)
                .start(stepOne)
                .on("*")
                .to(stepTwo)
                .from(stepOne).on("FAILED").to(stepThree)
                .end()
                .build();
    }

    @Bean
    @JobScope
    public Step stepOne(@Value("#{jobParameters[requestDate]}") String requestDate, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("jobFlowStepOne", jobRepository)
                .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
                    log.info(">>>>> This is JobFlowStepOne {}",requestDate);

                    contribution.setExitStatus(ExitStatus.FAILED);

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step stepTwo(@Value("#{jobParameters[requestDate]}") String requestDate, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("jobFlowStepTwo", jobRepository)
                .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
                    log.info(">>>>> This is jobFlowStepTwo {}",requestDate);

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step stepThree(@Value("#{jobParameters[requestDate]}") String requestDate, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("jobFlowStepThree", jobRepository)
                .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
                    log.info(">>>>> This is jobFlowStepThree {}",requestDate);

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
