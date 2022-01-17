package com.example.house.service.house.impl;

import com.example.house.base.ServiceResult;
import com.example.house.domain.*;
import com.example.house.dto.HouseDTO;
import com.example.house.dto.HouseDetailDTO;
import com.example.house.dto.HousePictureDTO;
import com.example.house.form.HouseForm;
import com.example.house.mapper.*;
import com.example.house.service.house.IHouseService;
import com.example.house.service.house.IQiNiuService;
import com.example.house.util.LoginUserUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HouseServiceImpl implements IHouseService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private HouseDetailMapper houseDetailMapper;

    @Autowired
    private HousePictureMapper housePictureMapper;

    @Autowired
    private HouseTagMapper houseTagMapper;

    @Autowired
    private SubwayMapper subwayMapper;

    @Autowired
    private SubwayStationMapper subwayStationMapper;

    @Autowired
    private IQiNiuService iQiNiuService;


    @Value("${qiniu.cdn.prefix}")
    private String cdnPrefix;


    @Override
    @Transactional
    public ServiceResult<HouseDTO> save(HouseForm houseForm) {
        HouseDetail detail = new HouseDetail();
        ServiceResult<HouseDTO> subwayValidationResult = wrapperDetailInfo(detail, houseForm);
        if(subwayValidationResult != null){
            return subwayValidationResult;
        }

        House house = new House();
        modelMapper.map(houseForm,house);

        LocalDateTime now = LocalDateTime.now();
        house.setCreateTime(now);
        house.setLastUpdateTime(now);
        house.setAdminId(LoginUserUtil.getLoginUserId().intValue());
        houseMapper.save(house);

        detail.setHouseId(house.getId());
        houseDetailMapper.save(detail);

        List<HousePicture> pictures = generatePictures(houseForm,house.getId());
        housePictureMapper.save(pictures);

        HouseDTO houseDTO = modelMapper.map(house,HouseDTO.class);
        HouseDetailDTO houseDetailDTO = modelMapper.map(detail,HouseDetailDTO.class);

        houseDTO.setHouseDetail(houseDetailDTO);

        List<HousePictureDTO> pictureDTOS = new ArrayList<>();
        pictures.forEach(housePicture -> pictureDTOS.add(modelMapper.map(housePicture,HousePictureDTO.class)));
        houseDTO.setPictures(pictureDTOS);
        houseDTO.setCover(this.cdnPrefix + houseDTO.getCover());

        List<String> tags = houseForm.getTags();
        if(tags != null && !tags.isEmpty()){
            List<HouseTag> houseTags = new ArrayList<>();
            for(String tag : tags){
                HouseTag houseTag = new HouseTag();
                houseTag.setHouseId(house.getId());
                houseTag.setName(tag);
                houseTags.add(houseTag);
            }
            houseTagMapper.save(houseTags);
            houseDTO.setTags(tags);
        }
        return ServiceResult.success(null,houseDTO);
    }


    @Override
    public ServiceResult update(HouseForm houseForm) {
        return null;
    }

    @Override
    public ServiceResult<HouseDTO> findCompleteOne(Long id) {
        return null;
    }

    @Override
    public ServiceResult addTag(Long houseId, String tag) {
        return null;
    }

    @Override
    public ServiceResult removeTag(Long houseId, String tag) {
        return null;
    }

    @Override
    public ServiceResult updateStatus(Long id, int status) {
        return null;
    }

    @Override
    public ServiceResult removePhoto(Long id) {
        return null;
    }

    @Override
    public ServiceResult updateCover(Long coverId, Long targetId) {
        return null;
    }

    private ServiceResult<HouseDTO> wrapperDetailInfo(HouseDetail detail, HouseForm houseForm) {
        HouseDetail houseDetail = new HouseDetail();
        Subway subway = subwayMapper.findOne(houseForm.getSubwayLineId());
        System.out.println("test1........................................");
        if(subway == null){
            return ServiceResult.of(false,"没有合适的地铁线路！");
        }
        System.out.println("test2........................................");
        SubwayStation subwayStation = subwayStationMapper.findOne(houseForm.getSubwayStationId());
        if(subwayStation == null || subway.getId() != subwayStation.getSubwayId()){
            return ServiceResult.of(false,"没有合适的地铁站！");
        }

        System.out.println("test1........................................");

        houseDetail.setSubwayLineId(subway.getId());
        houseDetail.setSubwayLineName(subway.getName());

        houseDetail.setSubwayStationId(subway.getId());
        houseDetail.setSubwayStationName(subway.getName());

        houseDetail.setDescription(houseForm.getDescription());
        houseDetail.setAddress(houseForm.getDetailAddress());
        houseDetail.setLayoutDesc(houseForm.getLayoutDesc());
        houseDetail.setRentWay(houseForm.getRentWay());
        houseDetail.setRoundService(houseForm.getRoundService());
        houseDetail.setTraffic(houseForm.getTraffic());
        return null;
    }

    private List<HousePicture> generatePictures(HouseForm houseForm, Integer id) {
        return null;
    }

}
