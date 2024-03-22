package com.springbatch.job;

import com.springbatch.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

@Slf4j
@RequiredArgsConstructor
public class CustomBatchItemWriter implements ItemWriter<MemberDto> {

    private final JdbcBatchItemWriter<MemberDto> jdbcBatchItemWriter;

    @Override
    public void write(Chunk<? extends MemberDto> items) throws Exception {
        for (MemberDto memberDto : items.getItems()) {
            if (memberDto.getMileage() > 10000) {
                memberDto.setGrade("VIP");
            }
        }

        jdbcBatchItemWriter.afterPropertiesSet();
        jdbcBatchItemWriter.write(items);
    }
}
