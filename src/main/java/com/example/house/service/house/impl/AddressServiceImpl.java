package com.example.house.service.house.impl;

import com.example.house.base.ServiceMultiResult;
import com.example.house.domain.Subway;
import com.example.house.domain.SubwayStation;
import com.example.house.domain.SupportAddress;
import com.example.house.dto.SubwayDTO;
import com.example.house.dto.SubwayStationDTO;
import com.example.house.dto.SupportAddressDTO;
import com.example.house.form.HouseForm;
import com.example.house.mapper.SubwayMapper;
import com.example.house.mapper.SubwayStationMapper;
import com.example.house.mapper.SupportAddressMapper;
import com.example.house.service.house.IAddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private SubwayStationMapper subwayStationMapper;

    @Autowired
    private SubwayMapper subwayMapper;

    @Autowired
    private SupportAddressMapper supportAddressMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ServiceMultiResult<SupportAddressDTO> findAllCities() {
        List<SupportAddress> addresses = supportAddressMapper.
                findAllByLevel(SupportAddress.Level.CITY.getValue());

        List<SupportAddressDTO> addressDTOS = new ArrayList<>();
        for(SupportAddress supportAddress : addresses){
            SupportAddressDTO target = modelMapper.map(supportAddress, SupportAddressDTO.class);
            addressDTOS.add(target);
        }
        return new ServiceMultiResult<>(addressDTOS.size(), addressDTOS);
    }

    @Override
    public ServiceMultiResult findAllRegionsByCityName(String cityName) {
        if (cityName == null){
            return new ServiceMultiResult<>(0,null);
        }
        List<SupportAddressDTO> result = new ArrayList<>();
        List<SupportAddress> regions = supportAddressMapper.
                findAllByLevelAndBelongTo(SupportAddress.Level.REGION.getValue(),cityName);
        for(SupportAddress region : regions){
            result.add(modelMapper.map(region,SupportAddressDTO.class));
        }
        return new ServiceMultiResult<>(result.size(),regions);
    }

    @Override
    public List<SubwayDTO> findAllSubwayByCity(String cityEnName) {
        List<SubwayDTO> result = new ArrayList<>();
        List<Subway> subways = subwayMapper.findAllByCityEnName(cityEnName);
        if(subways.isEmpty()){
            return result;
        }
        subways.forEach(subway->modelMapper.map(subways,SubwayStationDTO.class));
        return result;
    }

    @Override
    public List<SubwayStationDTO> findAllStationBySubway(Long subwayId) {
        List<SubwayStationDTO> result = new ArrayList<>();
        List<SubwayStation> stations = subwayStationMapper.findAllBySubWayId(subwayId);
        if(stations.isEmpty()){
            return result;
        } else {
         stations.forEach(station -> result.add(modelMapper.map(stations,SubwayStationDTO.class)));
         return result;
        }
    }

    @Override
    public Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName) {
        return null;
    }


}
