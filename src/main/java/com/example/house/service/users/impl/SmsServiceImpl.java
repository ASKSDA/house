package com.example.house.service.users.impl;

import com.example.house.base.ServiceResult;
import com.example.house.service.users.ISmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
public class SmsServiceImpl implements ISmsService {
    @Autowired
    private RedisTemplate<String ,String> redisTemplate;

    private final static String SMS_CODE_CONTENT_PREFIX = "SMS::CODE::CONTENT::";

    private static String generateRandomSmsCode(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++){
            sb.append(random.nextInt(10));
        } return sb.toString();
    }

    @Override
    public String getSmsCode(String telephone) {
        return redisTemplate.opsForValue().get(SMS_CODE_CONTENT_PREFIX + telephone);
    }

    @Override
    public void remove(String telephone) {
        redisTemplate.delete(SMS_CODE_CONTENT_PREFIX + telephone);
    }

    @Override
    public ServiceResult<String> sendSms(String telephone) {
        return null;
    }
}
