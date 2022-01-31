package com.example.house.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseTag {
    private Long houseId;
    private Long id;
    private String name;



    public HouseTag(Long houseId, String tag) {
        this.houseId = houseId;
        this.name = tag;
    }
}
