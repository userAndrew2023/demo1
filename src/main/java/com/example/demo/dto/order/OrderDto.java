package com.example.demo.dto.order;

import lombok.Data;

@Data
public class OrderDto {

    private Float quantity;
    private Float price;
    private Float dollars;
    private String type;
    private Long userId;
    private String stockSymbol;
    private String side;

}
