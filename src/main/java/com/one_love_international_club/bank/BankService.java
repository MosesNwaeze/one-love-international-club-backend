package com.one_love_international_club.bank;

import com.one_love_international_club.auth.entity.UserEntity;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankService {
    private final BankRepository bankRepository;
    private final ModelMapper modelMapper;
    private final SecurityService securityService;

    private static final String ERROR_MESSAGE = "You do not have the permission to perform this operation.";
    private static final String NOT_FOUND_MESSAGE = "Bank not found.";

    @Transactional
    public Response<BankDto> createBank(BankDto bankDto) {
        UserEntity currentUser = securityService.getCurrentUser();
        ClubOrganEntity clubOrgan = currentUser.getRoleEntity().getClubOrgan();

        if (Objects.isNull(clubOrgan)) {
            throw new ClubException(ErrorCode.ACCESS_DENIED, ERROR_MESSAGE);
        }

        if (!clubOrgan.getName().equalsIgnoreCase("Executive")) {
            throw new ClubException(ErrorCode.ACCESS_DENIED, ERROR_MESSAGE);
        }

        BankEntity bankEntity = modelMapper.map(bankDto, BankEntity.class);
        bankEntity.setCreatedBy(currentUser);

        BankEntity save = bankRepository.save(bankEntity);

        return Response.<BankDto>builder()
                .message("Bank created successfully")
                .data(modelMapper.map(save, BankDto.class))
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Transactional
    public Response<BankDto> updateBank(BankDto bankDto) {
        UserEntity currentUser = securityService.getCurrentUser();
        ClubOrganEntity clubOrgan = currentUser.getRoleEntity().getClubOrgan();

        if (Objects.isNull(clubOrgan)) {
            throw new ClubException(ErrorCode.ACCESS_DENIED, ERROR_MESSAGE);
        }

        if (!clubOrgan.getName().equalsIgnoreCase("Executive")) {
            throw new ClubException(ErrorCode.ACCESS_DENIED, ERROR_MESSAGE);
        }

        BankEntity bankEntity = bankRepository
                .findById(bankDto.getId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND, NOT_FOUND_MESSAGE));

        if (Objects.nonNull(bankDto.getName())) {
            bankEntity.setName(bankDto.getName());
        }

        if (Objects.nonNull(bankDto.getBalance())) {
            bankEntity.setBalance(bankDto.getBalance());
        }

        if (Objects.nonNull(bankDto.getAccountNumber())) {
            bankEntity.setAccountNumber(bankDto.getAccountNumber());
        }

        BankEntity save = bankRepository.save(bankEntity);

        log.info("Bank updated successfully, {}", save);

        return Response.<BankDto>builder()
                .timestamp(LocalDateTime.now())
                .data(modelMapper.map(save, BankDto.class))
                .message("Bank updated successfully")
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .build();
    }

    public Response<BankDto> getBank(UUID id) {
        BankEntity bankEntity = bankRepository
                .findById(id)
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND, NOT_FOUND_MESSAGE));

        log.info("Banks returned  successfully, {}", bankEntity);

        return Response.<BankDto>builder()
                .timestamp(LocalDateTime.now())
                .message("Bank found.")
                .data(modelMapper.map(bankEntity, BankDto.class))
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .build();
    }

    public Response<List<BankDto>> getBanks() {
        List<BankEntity> all = bankRepository.findAll();

        log.info("Banks found successfully, {}", all);

        return Response.<List<BankDto>>builder()
                .timestamp(LocalDateTime.now())
                .message("Banks found.")
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .data(all.stream().map(item -> modelMapper.map(all, BankDto.class)).toList())
                .build();
    }

    @Transactional
    public Response<Void> removeBank(UUID id) {
        UserEntity currentUser = securityService.getCurrentUser();
        ClubOrganEntity clubOrgan = currentUser.getRoleEntity().getClubOrgan();

        if (Objects.isNull(clubOrgan)) {
            throw new ClubException(ErrorCode.ACCESS_DENIED, ERROR_MESSAGE);
        }

        if (!clubOrgan.getName().equalsIgnoreCase("Executive")) {
            throw new ClubException(ErrorCode.ACCESS_DENIED, ERROR_MESSAGE);
        }

        bankRepository.deleteById(id);

        log.info("Bank with id: {} deleted successfully", id);

        return Response.<Void>builder()
                .timestamp(LocalDateTime.now())
                .message("Bank removed successfully")
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .build();

    }


}
