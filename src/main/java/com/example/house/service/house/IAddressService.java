package com.example.house.service.house;

import com.example.house.base.ServiceMultiResult;
import com.example.house.domain.SupportAddress;
import com.example.house.dto.SubwayDTO;
import com.example.house.dto.SubwayStationDTO;
import com.example.house.dto.SupportAddressDTO;
import com.example.house.form.HouseForm;

import java.util.List;
import java.util.Map;

public interface IAddressService {
    ServiceMultiResult<SupportAddressDTO> findAllCities(); //查看所有城市信息（得到city/address/Dto)

    ServiceMultiResult findAllRegionsByCityName(String cityEnName);//根据城市的名字（id，enName）得到所有的区域

    List<SubwayDTO> findAllSubwayByCity(String cityEnName);//根据城市的名字得到所有的地铁线路

    List<SubwayStationDTO> findAllStationBySubway(Long subwayId);//根据地铁线路的名字获得所有地铁站的信息

    Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName);
}
