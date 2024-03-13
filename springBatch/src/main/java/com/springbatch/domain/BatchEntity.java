package com.springbatch.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name="BATCH_TEST")
public class BatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Builder
    public BatchEntity(String name) {
        this.name = name;
    }
}
