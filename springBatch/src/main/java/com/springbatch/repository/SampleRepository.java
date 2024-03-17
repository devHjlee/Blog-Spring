package com.springbatch.repository;

import com.springbatch.domain.BatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<BatchEntity,Long> {
}
