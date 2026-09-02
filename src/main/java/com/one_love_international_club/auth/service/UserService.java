package com.one_love_international_club.auth.service;

import com.one_love_international_club.auth.dto.UserDto;
import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.auth.repo.UserRepository;
import com.one_love_international_club.security.SecurityService;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.Status;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @PersistenceContext
    private final EntityManager entityManager;

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final SecurityService securityService;


    public Response<PaginatedResponse<UserDto>> getPendingUsers(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> pages = userRepository
                .findUsers(Status.PENDING, search, pageable);

        return getUserByStatus(pages, page, size, "Pending members.");

    }


    public Response<UserDto> getCurrentUser() {
        UserEntity currentUser = securityService.getCurrentUser();

        UserDto userDto = modelMapper.map(currentUser, UserDto.class);

        return Response.<UserDto>builder()
                .data(userDto)
                .code(200)
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();

    }

    public Response<PaginatedResponse<UserDto>> getActiveUsers(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> pages = userRepository
                .findUsers(Status.ACTIVE, search, pageable);

        return getUserByStatus(pages, page, size, "Active members.");

    }

    public Response<PaginatedResponse<UserDto>> getExecutiveUsers(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> pages = userRepository
                .findExecutiveUsers("Executive", search, pageable);

        return getUserByStatus(pages, page, size, "Executive members.");

    }

    public Response<PaginatedResponse<UserDto>> getAllUsers(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> pages = userRepository
                .findAllUsers(search, pageable);

        return getUserByStatus(pages, page, size, "All members.");

    }


    private Response<PaginatedResponse<UserDto>> getUserByStatus(Page<UserEntity> pages, int page, int size, String message) {

        PaginatedResponse<UserDto> response = new PaginatedResponse<>();
        response.setPage(page);
        response.setSize(size);
        response.setTotalElements(pages.getTotalElements());
        response.setTotalPages(pages.getTotalPages());
        response.setContent(pages.getContent().stream().map(item -> modelMapper.map(item, UserDto.class)).toList());
        response.setLast(pages.isLast());
        response.setFirst(pages.isFirst());

        return Response.<PaginatedResponse<UserDto>>builder()
                .code(HttpStatus.OK.value())
                .message(message)
                .status(Status.SUCCESSFUL)
                .timestamp(LocalDateTime.now())
                .data(response)
                .build();
    }


}
