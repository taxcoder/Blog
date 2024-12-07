package com.tanx.blog.entity.dto;

import lombok.Data;

@Data
public class CountQuantityDto {
    private String name;
    private long num;

    public CountQuantityDto(String name, long num) {
        this.name = name;
        this.num = num;
    }
}
