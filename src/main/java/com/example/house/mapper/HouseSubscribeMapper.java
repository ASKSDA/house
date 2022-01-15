package com.example.house.mapper;

import com.example.house.domain.HouseSubscribe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseSubscribeMapper {
    HouseSubscribe findByHouseIdAndUserId(@Param("houseId") Long houseId,@Param("loginUserId") Long loginUserId);
    List<HouseSubscribe> findAllByUserIdAndStatus(@Param(value="userId")Long userId, @Param(value ="status") String status);
    List<HouseSubscribe> findAllByAdminIdAndStatus(@Param(value="adminId")Long adminId, @Param(value ="status") String status);
    HouseSubscribe findByHouseIdAndAdminId(@Param("houseId") Long houseId,@Param("adminId")Long adminId);
    int updateStatus(@Param(value="id") Long id,@Param(value="status") int status);

}
