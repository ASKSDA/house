package com.example.house.service.users.impl;

import com.example.house.base.ServiceResult;
import com.example.house.domain.Role;
import com.example.house.domain.User;
import com.example.house.dto.UserDTO;
import com.example.house.mapper.RoleMapper;
import com.example.house.mapper.UserMapper;
import com.example.house.service.users.IUserService;
import com.example.house.util.LoginUserUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUserByName(String userName) {
        User user = userMapper.findByName(userName);
        if(user == null){return null;}

        List<Role> roles = roleMapper.findRoleByUserId(user.getId());
        if(roles == null || roles.isEmpty()){
            throw new DisabledException("权限非法");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
        user.setAuthorityList(authorities);
        return user;
    }

    @Override
    public User findUserByTelephone(String telephone) {
        User user = userMapper.findUserByPhoneNumber(telephone);
        if(user == null){return null;}

        List<Role> roles = roleMapper.findRoleByUserId(user.getId());
        if(roles == null || roles.isEmpty()){
            throw new DisabledException("权限非法");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
        user.setAuthorityList(authorities);
        return user;

    }

    @Override
    public ServiceResult<UserDTO> findById(Long userId) {
        User user = userMapper.findOne(userId);
        if(user == null)
            return ServiceResult.notFound();
        UserDTO userDTO = modelMapper.map(user,UserDTO.class);

        return ServiceResult.of(userDTO);
    }

    @Override
    @Transactional
    public User addUserByPhone(String telephone) {
        User user = new User();
        user.setPhoneNumber(telephone);
        user.setName(telephone.substring(0,3) + "****" + telephone.substring(7));
        user.setStatus(0);
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setLastLoginTime(now);
        user.setLastUpdateTime(now);
        userMapper.save(user);

        Role role = new Role();
        role.setName("USER");
        role.setUserId(user.getId());
        roleMapper.save(role);

        user.setAuthorityList(List.of(new SimpleGrantedAuthority("ROLE_USER")));
        return user;
    }

    @Override
    public ServiceResult<User> modifyUserProfile(String profile, String value) {
        Long userId = LoginUserUtil.getLoginUserId();
        if(profile == null || profile.isEmpty()){
            return ServiceResult.of(false,"属性不能为空！");
        }
        switch(profile){
            case "name":
                userMapper.updateUsername(userId,value);
                break;
            case "email":
                userMapper.updateEmail(userId,value);
                break;
            case "password":
                userMapper.updatePassword(userId,passwordEncoder.encode(value));
                break;
            default:
                return ServiceResult.of(false,"不支持的属性！");
        }
        return ServiceResult.success();
    }


}
