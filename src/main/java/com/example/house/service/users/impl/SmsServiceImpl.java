package com.example.house.service.users.impl;

import com.example.house.base.ServiceResult;
import com.example.house.service.users.ISmsService;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements ISmsService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final static String SMS_CODE_CONTENT_PREFIX = "SMS::CODE::CONTENT::";

    private static String generateRandomSmsCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
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
        String code = generateRandomSmsCode();
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://gbk.api.smschinese.cn");
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=gbk");
        NameValuePair[] data = {
                new NameValuePair("Uid", "houjinshan"),
                new NameValuePair("Key", "d41d8cd98f00b204e980"),
                new NameValuePair("smsMob", telephone),
                new NameValuePair("smsText", "验证码" + code),
        };
        post.setRequestBody(data);

        try {
            client.executeMethod(post);
            String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
            post.releaseConnection();
            redisTemplate.opsForValue().set("SMS::CODE::CONTENT::" + telephone,code,10, TimeUnit.MINUTES);
            return ServiceResult.of(true,code,"短信发送成功！");
        } catch (IOException e) {
            e.printStackTrace();
            return ServiceResult.of(false, code, "短信发送失败！");
        }
    }
}