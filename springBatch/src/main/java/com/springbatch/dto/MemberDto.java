package com.springbatch.dto;

import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String name;
    private String grade;
    private int mileage;
}
