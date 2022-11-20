package com.fvbackground.entity;

import lombok.Data;

@Data
public class ProductInformation {
    private Integer id;
    private String logId;
    private String username;
    private String name;
    private double price;
    private String link;
    private String shop;
    private String introduction;
}
