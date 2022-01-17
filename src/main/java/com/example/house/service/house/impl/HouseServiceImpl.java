package com.example.house.service.house.impl;

import com.example.house.base.ServiceResult;
import com.example.house.domain.HouseDetail;
import com.example.house.dto.HouseDTO;
import com.example.house.form.HouseForm;
import com.example.house.service.house.IHouseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HouseServiceImpl implements IHouseService {

    @Override
    @Transactional
    public ServiceResult<HouseDTO> save(HouseForm houseForm) {
        HouseDetail detail = new HouseDetail();
        return null;
    }
}
