package com.example.house;

import com.example.house.domain.User;
import com.example.house.mapper.RoleMapper;
import com.example.house.mapper.UserMapper;
import com.example.house.service.users.ISmsService;
import com.example.house.service.users.impl.SmsServiceImpl;
import com.example.house.service.users.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootTest
class HouseApplicationTests {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SmsServiceImpl smsService;

    @Autowired
    private UserServiceImpl userService;

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
        smsService.remove("13831684810");
    }

}
