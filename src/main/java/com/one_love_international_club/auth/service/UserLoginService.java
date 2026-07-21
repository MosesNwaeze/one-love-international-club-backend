package com.one_love_international_club.auth.service;

import com.one_love_international_club.auth.entity.UserLoginEntity;
import com.one_love_international_club.auth.repo.UserLoginRepository;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.security.SecurityService;
import com.one_love_international_club.security.TwoFactorAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final ModelMapper modelMapper;
    private final UserLoginRepository userLoginRepository;
    private final PasswordEncoder passwordEncoder;
    private final TwoFactorAuthService twoFactorAuthService;
    private final SecurityService securityService;

    private static final String USER_LOGIN_NOT_FOUND = "User Login not found";
    private static final String DEFAULT_APPLICATION_NAME = "Default Application";



    public UserLoginEntity getUserLoginById(UUID userLoginId) {

        return userLoginRepository.findById(userLoginId)
            .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND, "User login not found"));
    }



}
