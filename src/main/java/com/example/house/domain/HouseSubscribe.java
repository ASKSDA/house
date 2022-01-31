package com.example.house.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HouseSubscribe {
    private Long id;
    private Long houseId;
    private Long userId;
    private String desc;
    private Integer status;
    private LocalDateTime creatTime;
    private LocalDateTime lastUpdateTime;
    private LocalDateTime orderTime;
    private String telephone;
    private Long adminId;
}
