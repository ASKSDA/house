package com.example.house.mapper;

import com.example.house.domain.SubwayStation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubwayStationMapper {
    List<SubwayStation> findAllBySubWayId(Long subwayId);
    SubwayStation findOne(@Param(value="id") Long subwayStationId);
}
