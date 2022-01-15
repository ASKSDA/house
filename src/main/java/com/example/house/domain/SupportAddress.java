package com.example.house.domain;

import lombok.Data;

@Data
public class SupportAddress {
    private Integer id;
    private String belongTo;
    private String enName;
    private String cnName;
    private String level;
    private Double baiduMapLongitude;
    private Double baiduMapLatitude;


}
