package com.one_love_international_club.auth.service;

import com.one_love_international_club.auth.dto.UserDto;
import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.auth.repo.UserRepository;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.security.SecurityService;
import com.one_love_international_club.security.TwoFactorAuthService;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @PersistenceContext
    private final EntityManager entityManager;

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    public void performQuery(){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<UserEntity> query = builder.createQuery(UserEntity.class);

        Root<UserEntity> user = query.from(UserEntity.class);



        CriteriaQuery<UserEntity> select = query.select(user);
    }


}
