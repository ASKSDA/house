package com.example.house.controller;

import com.example.house.base.ApiResponse;
import com.example.house.base.QiNiuPutRet;
import com.example.house.base.ServiceResult;
import com.example.house.dto.UserDTO;
import com.example.house.service.house.impl.QiNiuServiceImpl;
import com.example.house.service.users.impl.UserServiceImpl;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
public class AdminController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private QiNiuServiceImpl qiNiuService;

    @Autowired
    private Gson gson;


    @GetMapping("/admin/login")
    public String adminLoginPage() {
        return "admin/login";
    }

    @GetMapping("admin/add/house")
    public String addHousePage() {
        return "admin/house-add";
    }

    @PostMapping(value = "admin/upload/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ApiResponse uploadPhoto(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAM);
        }

        String fileName = file.getOriginalFilename();

        try {
            InputStream inputStream = file.getInputStream();
            Response response = qiNiuService.uploadFile(inputStream);
            if (response.isOK()) {
                QiNiuPutRet ret = gson.fromJson(response.bodyString(), QiNiuPutRet.class);
                return ApiResponse.ofSuccess(ret);
            } else {
                return ApiResponse.ofMessage(response.statusCode, response.getInfo());
            }

        } catch (QiniuException e) {
            Response response = e.response;
            try {
                return ApiResponse.ofMessage(response.statusCode, response.bodyString());
            } catch (QiniuException e1) {
                e1.printStackTrace();
                return ApiResponse.ofStatus(ApiResponse.Status.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return ApiResponse.ofStatus(ApiResponse.Status.INTERNAL_SERVER_ERROR);
        }


    }

    @GetMapping("admin/user/userId")
    @ResponseBody
    public ApiResponse getUserInfo(@PathVariable(value="userId") Long userId){
        if (userId == null || userId < 1){
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        ServiceResult<UserDTO> serviceResult = userService.findById(userId);
        if(!serviceResult.isSuccess()){
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_FOUND);
        } else {
            return ApiResponse.ofSuccess(serviceResult.getResult());
        }

    }
}