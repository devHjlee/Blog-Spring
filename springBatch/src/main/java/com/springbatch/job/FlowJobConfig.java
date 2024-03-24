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
public class FlowJobConfig {

    @Bean
    public Job flowJob(JobRepository jobRepository, Step stepOne, Step stepTwo, Step stepThree) {
        return new JobBuilder("flowJob", jobRepository)
                .start(stepOne)                             // StepOne으로 시작
                .on("*")                             // 모든 결과에 대해서
                .to(stepTwo)                               // StepTwo로 이동
                .from(stepOne).on("FAILED").to(stepThree)  // StepOne이 실패한 경우에만 StepThree로 이동
                .end()                                     // Flow 종료
                .build();
    }

    @Bean
    @JobScope
    public Step stepOne(@Value("#{jobParameters[reqDt]}") String requestDate, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("flowJob_stepOne", jobRepository)
                .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
                    log.info(">>>>> This is flowJob_stepOne {}",requestDate);

                    contribution.setExitStatus(ExitStatus.FAILED);

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step stepTwo(@Value("#{jobParameters[reqDt]}") String requestDate, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("flowJob_stepTwo", jobRepository)
                .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
                    log.info(">>>>> This is flowJob_stepTwo {}",requestDate);

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step stepThree(@Value("#{jobParameters[reqDt]}") String requestDate, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("flowJob_stepThree", jobRepository)
                .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
                    log.info(">>>>> This is flowJob_stepThree {}",requestDate);

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
