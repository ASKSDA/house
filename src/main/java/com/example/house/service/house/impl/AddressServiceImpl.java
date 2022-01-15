package com.example.house.service.house.impl;

import com.example.house.base.ServiceMultiResult;
import com.example.house.domain.SupportAddress;
import com.example.house.dto.SubwayDTO;
import com.example.house.dto.SubwayStationDTO;
import com.example.house.dto.SupportAddressDTO;
import com.example.house.mapper.SupportAddressMapper;
import com.example.house.service.house.IAddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private SupportAddressMapper supportAddressMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ServiceMultiResult<SupportAddressDTO> findAllCities() {
        List<SupportAddress> addresses = supportAddressMapper.findAllByLevel(SupportAddress.Level.CITY.getValue());

        List<SupportAddressDTO> addressDTOS = new ArrayList<>();
        for(SupportAddress supportAddress : addresses){
            SupportAddressDTO target = modelMapper.map(supportAddress, SupportAddressDTO.class);
            addressDTOS.add(target);
        }
        return new ServiceMultiResult<>(addressDTOS.size(), addressDTOS);
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
