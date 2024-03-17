package com.springbatch.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
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
