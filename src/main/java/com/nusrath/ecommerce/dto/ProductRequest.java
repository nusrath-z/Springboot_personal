package com.nusrath.ecommerce.dto;

import java.math.BigDecimal;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;

}
