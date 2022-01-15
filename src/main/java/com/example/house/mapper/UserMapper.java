package com.example.house.mapper;

import com.example.house.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int save(User user);
    User findOne(Long id);
    User findByName(String userName);
    User findUserByPhoneNumber(String telephone);
    int updateUsername(@Param("id") Long id, @Param(value = "name") String name);
    int updateEmail(@Param("id") Long id, @Param(value = "email") String email);
    int updatePassword(@Param("id") Long id, @Param(value = "password") String password);
}

