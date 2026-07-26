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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Transactional
    public Response<UserDto> updateUser(UserDto userDto) {
        UserEntity user = userRepository
                .findById(userDto.getId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "User with  id " + userDto.getId() + " not found"));

        modelMapper.map(userDto, user);
        UserEntity save = userRepository.save(user);

        return Response.<UserDto>builder()
                .code(HttpStatus.OK.value())
                .data(modelMapper.map(save, UserDto.class))
                .message("User updated successfully.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }


}
