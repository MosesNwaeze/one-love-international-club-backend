package com.one_love_international_club.resignation;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.enums.ResignationStatus;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.security.SecurityService;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.Status;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import com.one_love_international_club.setting.repo.AppRepository;
import com.one_love_international_club.util.EmailService;
import com.one_love_international_club.util.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ResignationService {

    private final ResignationRepository resignationRepository;
    private final ModelMapper modelMapper;
    private final SecurityService securityService;
    private final FileService fileService;
    private final EmailService emailService;
    private final AppRepository appRepository;

    private static final String UPLOAD_PATH = "resignation";

    @Transactional
    public Response<ResignationDto> resign(ResignationDto resignationDto) {

        if (resignationDto.getBenefit().getCreatedAt().plusYears(3).isBefore(LocalDateTime.now())) {
            throw new ClubException(ErrorCode.ACCESS_DENIED, "You cannot resign now until after 3 years.");
        }

        resignationDto.setResignationStatus(ResignationStatus.PENDING);

        UserEntity currentUser = securityService.getCurrentUser();

        ResignationEntity resignationEntity = modelMapper.map(resignationDto, ResignationEntity.class);

        resignationEntity.setUser(currentUser);

        Map<String, String> uploaded = fileService.uploadBase64Image(resignationDto.getResignationLetter(), UPLOAD_PATH);

        resignationEntity.setResignationLetter(uploaded.get("fileUrl"));
        resignationEntity.setResignationLetterPublicId(uploaded.get("publicId"));

        ResignationEntity saved = resignationRepository.save(resignationEntity);

        log.info("You resignation is awaiting approval, {}", resignationEntity);

        return Response.<ResignationDto>builder()
                .message("You resignation is awaiting approval")
                .code(HttpStatus.CREATED.value())
                .data(modelMapper.map(saved, ResignationDto.class))
                .status(Status.SUCCESSFUL)
                .timestamp(LocalDateTime.now())
                .build();

    }

    @Transactional
    public Response<ResignationDto> approve(UUID resignationId, String rejectionReason, ResignationStatus status) {
        ResignationEntity resignationEntity = resignationRepository
                .findById(resignationId)
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Resignation with id: " + resignationId + " not found."));

        resignationEntity.setResignationStatus(status);
        resignationEntity.setRejectionReason(rejectionReason);

        ResignationEntity saved = resignationRepository.save(resignationEntity);

        log.info("Resignation has been approved successfully, {}", saved);

        UserEntity user = saved.getUser();

        assert user != null;

        String body = "Your request to resign from ONE LOVE INTERNATIONAL NOBLE CLUB has been approve with status:" +
                saved.getResignationStatus().name();

        emailService.sendEmail(user.getEmail(), "Resignation approval", body);

        return Response.<ResignationDto>builder()
                .timestamp(LocalDateTime.now())
                .message("Resignation has been approved successfully.")
                .code(HttpStatus.OK.value())
                .data(modelMapper.map(saved, ResignationDto.class))
                .status(Status.SUCCESSFUL)
                .build();
    }


    public Response<PaginatedResponse<ResignationDto>> getAllResignation(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<ResignationEntity> pages = resignationRepository.findAll(pageable);

        PaginatedResponse<ResignationDto> response = new PaginatedResponse<>();
        response.setPage(page);
        response.setSize(size);
        response.setTotalPages(pages.getTotalPages());
        response.setTotalElements(pages.getTotalElements());
        response.setContent(pages.getContent().stream().map(item -> modelMapper.map(item, ResignationDto.class))
                .toList());

        log.info("All resignation has been returned. {}", response);

        return Response.<PaginatedResponse<ResignationDto>>builder()
                .data(response)
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("All resignations has been returned.")
                .build();
    }


}
