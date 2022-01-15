package com.example.house.mapper;

import com.example.house.domain.House;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface HouseMapper {
    int updateCover(@Param(value="id") Long id, @Param(value = "cover") String cover);
    int updateStatus(@Param(value="id") Long id, @Param(value = "status") int status);
    int updateWatchTimes(@Param(value="id") Long houseId);
    int save(House house);
    House findOne(Long id);
    List<House> findAll();
//    List<House> findByDatatableSearch(DatatableSearch searchBody);

}
