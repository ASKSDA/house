package com.example.house.domain;

import lombok.Data;

@Data
public class HouseDetail {
    private Long id;
    private Long houseId;
    private String description;
    private String layoutDesc;
    private String traffic;
    private String roundService;
    private Integer rentWay;
    private String address;
    private Long subwayLineId;
    private String subwayLineName;
    private Long subwayStationId;
    private String subwayStationName;

}
