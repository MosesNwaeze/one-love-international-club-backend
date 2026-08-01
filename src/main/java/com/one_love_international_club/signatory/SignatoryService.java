package com.one_love_international_club.signatory;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.bank.BankEntity;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.security.SecurityService;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.Status;
import com.one_love_international_club.setting.entity.ClubOrganEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SignatoryService {

    private final SignatoryRepository signatoryRepository;
    private final ModelMapper modelMapper;
    private final SecurityService securityService;

    private static final String ERROR_MESSAGE = "You are not allowed to create a signatory";

    @Transactional
    @PreAuthorize("hasRole('')")
    public Response<SignatoryDto> createSignatory(SignatoryDto signatoryDto) {

        UserEntity user = securityService.getCurrentUser();

        ClubOrganEntity clubOrgan = user.getRoleEntity().getClubOrgan();

        if (Objects.isNull(clubOrgan)) {
            throw new ClubException(ErrorCode.ACCESS_DENIED, ERROR_MESSAGE);
        }

        if (!clubOrgan.getName().equalsIgnoreCase("Executive")) {
            throw new ClubException(ErrorCode.ACCESS_DENIED, ERROR_MESSAGE);
        }

        SignatoryEntity signatory = modelMapper.map(signatoryDto, SignatoryEntity.class);
        signatory.setCreatedBy(user);
        SignatoryEntity save = signatoryRepository.save(signatory);

        log.info("Signatory created successfully, by {}", user);

        return Response.<SignatoryDto>builder()
                .data(modelMapper.map(save, SignatoryDto.class))
                .code(HttpStatus.CREATED.value())
                .message("Signatory created.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();

    }

    @Transactional
    public Response<SignatoryDto> updateSignatory(SignatoryDto signatoryDto) {
        UserEntity user = securityService.getCurrentUser();
        ClubOrganEntity clubOrgan = user.getRoleEntity().getClubOrgan();
        if (Objects.isNull(clubOrgan)) {
            throw new ClubException(ErrorCode.ACCESS_DENIED, ERROR_MESSAGE);
        }

        if (!clubOrgan.getName().equalsIgnoreCase("Executive")) {
            throw new ClubException(ErrorCode.ACCESS_DENIED, ERROR_MESSAGE);
        }

        SignatoryEntity signatory = signatoryRepository
                .findById(signatoryDto.getId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND, "Signatory not found"));

        if (Objects.nonNull(signatoryDto.getSignatory())) {
            signatory.setSignatory(modelMapper.map(signatoryDto.getSignatory(), UserEntity.class));
        }

        if (Objects.nonNull(signatoryDto.getBank())) {
            signatory.setBank(modelMapper.map(signatoryDto.getBank(), BankEntity.class));
        }

        SignatoryEntity save = signatoryRepository.save(signatory);
        log.info("Signatory updated successfully, by {}", user);

        return Response.<SignatoryDto>builder()
                .data(modelMapper.map(save, SignatoryDto.class))
                .code(HttpStatus.OK.value())
                .message("Signatory updated.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }

    public Response<SignatoryDto> getSignatory(UUID id) {
        SignatoryEntity signatory = signatoryRepository
                .findById(id)
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND, "Signatory not found for id " + id));

        return Response.<SignatoryDto>builder()
                .data(modelMapper.map(signatory, SignatoryDto.class))
                .code(HttpStatus.OK.value())
                .message("Signatory found.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }

    public Response<List<SignatoryDto>> getSignatories() {
        List<SignatoryEntity> all = signatoryRepository.findAll();

        return Response.<List<SignatoryDto>>builder()
                .data(all.stream().map(sig -> modelMapper.map(sig, SignatoryDto.class)).toList())
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("Signatories found successfully")
                .status(Status.SUCCESSFUL)
                .build();
    }

    @Transactional
    public Response<Void> removeSignatory(UUID id) {
        UserEntity user = securityService.getCurrentUser();
        ClubOrganEntity clubOrgan = user.getRoleEntity().getClubOrgan();

        if (Objects.isNull(clubOrgan)) {
            throw new ClubException(ErrorCode.ACCESS_DENIED, ERROR_MESSAGE);
        }

        if (!clubOrgan.getName().equalsIgnoreCase("Executive")) {
            throw new ClubException(ErrorCode.ACCESS_DENIED, ERROR_MESSAGE);
        }

        signatoryRepository.deleteById(id);

        return Response.<Void>builder()
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .message("Signatory removed successfully.")
                .build();
    }
}
