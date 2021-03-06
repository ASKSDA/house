package com.example.house.mapper;

import com.example.house.domain.Subway;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SubwayMapper {
    List<Subway> findAllByCityEnName(String cityEnName);
    Subway findOne(@Param(value="id") Long subwayId);
}
