package com.example.house.mapper;

import com.example.house.domain.SubwayStation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubwayStationMapper {
    List<SubwayStation> findAllBYSubWayId(Long subwayId);
    SubwayStation findOne(Long subwayStationId);
}
