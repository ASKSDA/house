package com.example.house.security;

import com.example.house.domain.User;
import com.example.house.service.users.ISmsService;
import com.example.house.service.users.IUserService;
import com.example.house.util.LoginUserUtil;
import org.elasticsearch.common.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


public class AuthFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private IUserService userService;
    @Autowired
    private ISmsService smsService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        String name = super.obtainUsername(request);
        if(name != null && !"".equals(name)){
            request.setAttribute("username", name);
            return super.attemptAuthentication(request,response);
        }

        String telephone = request.getParameter("telephone");
        if(Strings.isNullOrEmpty(telephone) || !LoginUserUtil.checkTelephone(telephone)){
            throw new BadCredentialsException("Wrong telephone number");
        }

        User user = userService.findUserByTelephone(telephone);
        String inputCode = request.getParameter("smsCode");
        String sessionCode = smsService.getSmsCode(telephone);
        if(Objects.equals(inputCode,sessionCode)){
            if(user == null){
                user = userService.addUserByPhone(telephone);
            }
            return new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        } else {
            throw new BadCredentialsException("smsCodeError");
        }
    }
}
