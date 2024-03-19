package com.springbatch.job;

import com.springbatch.dto.BatchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JdbcJobConfig {
    private final DataSource dataSource;

    private static final int chunkSize = 10;

    @Bean
    public Job jdbcJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, Step jdbcStep) {
        return new JobBuilder("jdbcJob", jobRepository)
                .start(jdbcStep)
                .build();
    }

    @Bean
    @JobScope
    public Step jdbcStep(@Value("#{jobParameters[requestDate]}") String requestDate, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("jdbcStep", jobRepository)
                .<BatchDto,BatchDto>chunk(chunkSize,transactionManager)
                .reader(jdbcItemReader())
                .writer(jdbcItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<BatchDto> jdbcItemReader() {
        return new JdbcCursorItemReaderBuilder<BatchDto>()
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(BatchDto.class))
                .sql("SELECT id, name FROM BATCH_TEST")
                //.preparedStatementSetter(new ArgumentPreparedStatementSetter(new String[]{"aaa", "bbb"}))
                .name("jdbcItemReader")
                .build();
    }

//    @Bean
//    public JdbcBatchItemWriter<BatchDto> jdbcItemWriter() {
//        String sql = "UPDATE BATCH_TEST set name = ? where id = ?";
//        return new JdbcBatchItemWriterBuilder<BatchDto>().dataSource(dataSource)
//                .sql(sql)
//                .itemPreparedStatementSetter((item, ps) -> {
//                    ps.setString(1, "SYSTEM");
//                    ps.setLong(2, item.getId());
//                })
//
//                .assertUpdates(true)
//                .build();
//    }

    @Bean
    public JdbcBatchItemWriter<BatchDto> jdbcItemWriter() {
        String sql = "UPDATE BATCH_TEST set name = '3' where id = :id";
        return new JdbcBatchItemWriterBuilder<BatchDto>().dataSource(dataSource)
                .sql(sql)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .assertUpdates(true)
                .build();
    }
}
