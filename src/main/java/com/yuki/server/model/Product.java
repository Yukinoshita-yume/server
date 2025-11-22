package com.yuki.server.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private final String productCode;
    private final String name;
    private final BigDecimal fullPrice;
}
