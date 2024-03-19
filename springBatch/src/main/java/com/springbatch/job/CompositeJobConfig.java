package com.springbatch.job;

import com.springbatch.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CompositeJobConfig {
    private final DataSource dataSource;

    private static final int chunkSize = 10;

    @Bean
    public Job compositeJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, Step compositeStep) {
        return new JobBuilder("compositeJob", jobRepository)
                .start(compositeStep)
                .build();
    }

    @Bean
    @JobScope
    public Step compositeStep(@Value("#{jobParameters[reqDt]}") String requestDate, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("compositeStep", jobRepository)
                .<MemberDto,MemberDto>chunk(chunkSize,transactionManager)
                .reader(compositeItemReader())
                .writer(compositeWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<MemberDto> compositeItemReader() {
        return new JdbcCursorItemReaderBuilder<MemberDto>()
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(MemberDto.class))
                .sql("SELECT id, name, grade, mileage FROM MEMBER")
                .name("jdbcItemReader")
                .build();
    }

    @Bean
    @StepScope
    public CompositeItemWriter<MemberDto> compositeWriter() {
        final CompositeItemWriter<MemberDto> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(Arrays.asList(updateGrade(),insertVIP()));
        return compositeItemWriter;
    }

    private ItemWriter<MemberDto> updateGrade() {
        String sql = "update MEMBER set grade = :grade where id = :id";
        return items -> {
            JdbcBatchItemWriter<MemberDto> itemWriter = new JdbcBatchItemWriterBuilder<MemberDto>()
                    .dataSource(dataSource)
                    .sql(sql)
                    .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                    .build();

            for (MemberDto item : items) {
                if (item.getMileage() > 10000) {
                    item.setGrade("A");
                } else if (item.getMileage() > 1000) {
                    item.setGrade("B");
                }
            }
            itemWriter.afterPropertiesSet();
            itemWriter.write(items);
        };
    }

    private ItemWriter<MemberDto> insertVIP() {
        String sql = "insert into VIP(id,name) values(:id, :name)";
        return items -> {
            JdbcBatchItemWriter<MemberDto> itemWriter = new JdbcBatchItemWriterBuilder<MemberDto>()
                    .dataSource(dataSource)
                    .sql(sql)
                    .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                    .build();

            itemWriter.afterPropertiesSet();
            itemWriter.write(new Chunk<MemberDto>(items.getItems().stream().filter(item -> item.getMileage() > 1000).collect(Collectors.toList())));
        };
    }
}
