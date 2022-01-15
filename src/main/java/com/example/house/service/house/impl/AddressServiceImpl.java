package com.example.house.service.house.impl;

import com.example.house.base.ServiceMultiResult;
import com.example.house.dto.SubwayDTO;
import com.example.house.dto.SubwayStationDTO;
import com.example.house.dto.SupportAddressDTO;
import com.example.house.service.house.IAddressService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {
    @Override
    public ServiceMultiResult<SupportAddressDTO> findAllCities() {
        return null;
    }

    @Override
    public ServiceMultiResult findAllRegionsByCityName(String cityEnName) {
        return null;
    }

    @Override
    public List<SubwayDTO> findAllSubwayByCity(String cityEnName) {
        return null;
    }

    @Override
    public List<SubwayStationDTO> findAllStationBySubway(Long subwayId) {
        return null;
    }
}
