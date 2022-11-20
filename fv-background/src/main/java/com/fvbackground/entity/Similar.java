package com.fvbackground.entity;

import lombok.Data;

@Data
public class Similar {
    private Integer id;
    private String logId;
    private String username;
    private double score;
    private String brief;
    private String contSign;
}
