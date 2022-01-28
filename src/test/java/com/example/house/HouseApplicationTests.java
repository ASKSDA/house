package com.example.house;

import com.example.house.domain.*;
import com.example.house.mapper.*;
import com.example.house.service.house.impl.AddressServiceImpl;
import com.example.house.service.users.ISmsService;
import com.example.house.service.users.impl.SmsServiceImpl;
import com.example.house.service.users.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class HouseApplicationTests {
    @Autowired
    private AddressServiceImpl addressService;

    @Autowired
    private SupportAddressMapper supportAddressMapper;

    @Autowired
    private SubwayMapper subwayMapper;

    @Autowired
    private HouseTagMapper houseTagMapper;

    @Autowired
    private HouseSubscribeMapper houseSubscribeMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SmsServiceImpl smsService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    void contextLoads() {
        //System.out.println(passwordEncoder.encode("admin"));
        //System.out.println(passwordEncoder.encode("724441"));
        smsService.sendSms("13831684810");

    }

    @Test
    void x(){
        User user = new User();
        user.setId(3L);
        user.setEmail("@dong.com");
        user.setName("jack");
        user.setPhoneNumber("12300000000");
        user.setStatus(0);
        user.setCreateTime(LocalDateTime.now());
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastUpdateTime(LocalDateTime.now());
        userMapper.save(user);
    }

    @Test
    void xx(){
        System.out.println(userMapper.findOne(1L));
    }

    @Test
    void xxx() {
        System.out.println(roleMapper.findRoleByUserId(1L));
    }

    @Test
    void a(){
        System.out.println(houseSubscribeMapper.updateStatus(1L,7));
    }

    @Test
    void b(){
        System.out.println(addressService.findAllRegionsByCityName("sjz"));

    }

    @Test
    void c(){
        System.out.println(subwayMapper.findOne(2L));
    }

    @Test
    void d(){
        System.out.println(supportAddressMapper.findAllByLevelAndBelongTo("city","hb"));
    }

    @Test
    void e(){
        System.out.println(addressService.findAllCities());
    }
}
