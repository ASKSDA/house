package com.example.house.controller;

import com.example.house.base.ApiResponse;
import com.example.house.base.ServiceMultiResult;
import com.example.house.dto.SupportAddressDTO;
import com.example.house.service.house.impl.AddressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HouseController {
    @Autowired
    private AddressServiceImpl addressService;

    @RequestMapping("address/support/cities")
    @ResponseBody
    public ApiResponse getSupportCities(){
        ServiceMultiResult<SupportAddressDTO> result = addressService.findAllCities();
        if(result.getResultSize() == 0){
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_FOUND);
        }
        return ApiResponse.ofSuccess(result.getResult());
    }
}
