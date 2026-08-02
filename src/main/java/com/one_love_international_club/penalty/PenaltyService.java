package com.one_love_international_club.penalty;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.auth.repo.UserRepository;
import com.one_love_international_club.bank.BankEntity;
import com.one_love_international_club.due.DueEntity;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.security.SecurityService;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.Status;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import com.one_love_international_club.util.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PenaltyService {
    private final PenaltyRepository penaltyRepository;
    private final SecurityService securityService;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final UserRepository userRepository;

    private static final String UPLOAD_PATH = "incomes";

    @Transactional
    public Response<PenaltyDto> createIncome(PenaltyDto penaltyDto) {
        UserEntity currentUser = securityService.getCurrentUser();

        Map<String, String> uploaded = fileService
                .uploadBase64Image(penaltyDto.getProofOfPayment(), UPLOAD_PATH);

        PenaltyEntity penaltyEntity = modelMapper.map(penaltyDto, PenaltyEntity.class);
        penaltyEntity.setProofOfPayment(uploaded.get("fileUrl"));
        penaltyEntity.setProofOfPaymentPublicId(uploaded.get("publicId"));
        penaltyEntity.setPaidBy(currentUser.getId());

        PenaltyEntity income = penaltyRepository.save(penaltyEntity);

        modelMapper.map(income, penaltyDto);

        UserEntity paymentMadeBy = userRepository
                .findById(income.getPaidBy()).orElse(null);

        assert paymentMadeBy != null;
        penaltyDto.setMadeBy(getPaidBy(paymentMadeBy));

        return Response.<PenaltyDto>builder()
                .data(penaltyDto)
                .code(HttpStatus.CREATED.value())
                .message("Income created.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }

    @Transactional
    public Response<PenaltyDto> updateIncome(PenaltyDto penaltyDto) {
        PenaltyEntity penaltyEntity = penaltyRepository
                .findById(penaltyDto.getId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Income with id: " + penaltyDto.getId() + " not found."));
        if (Objects.nonNull(penaltyDto.getDue())) {
            penaltyEntity.setDue(modelMapper.map(penaltyDto.getDue(), DueEntity.class));
        }

        if (StringUtils.isNotBlank(penaltyDto.getProofOfPayment()) && StringUtils.isNotBlank(penaltyDto.getProofOfPaymentPublicId())) {
            fileService.deleteFile(penaltyDto.getProofOfPaymentPublicId());
            Map<String, String> uploaded = fileService
                    .uploadBase64Image(penaltyDto.getProofOfPayment(), UPLOAD_PATH);
            penaltyEntity.setProofOfPayment(uploaded.get("fileUrl"));
            penaltyEntity.setProofOfPaymentPublicId(uploaded.get("publicId"));
        }

        if (Objects.nonNull(penaltyDto.getBank())) {
            penaltyEntity.setBank(modelMapper.map(penaltyDto.getBank(), BankEntity.class));
        }

        if (Objects.nonNull(penaltyDto.getAmount())) {
            penaltyEntity.setAmount(penaltyDto.getAmount());
        }

        PenaltyEntity save = penaltyRepository.save(penaltyEntity);
        modelMapper.map(save, penaltyDto);

        UserEntity paymentMadeBy = userRepository
                .findById(save.getPaidBy()).orElse(null);

        assert paymentMadeBy != null;
        penaltyDto.setMadeBy(getPaidBy(paymentMadeBy));

        return Response.<PenaltyDto>builder()
                .data(penaltyDto)
                .code(HttpStatus.OK.value())
                .message("Income updated.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();

    }

    public Response<PenaltyDto> getIncomeById(UUID id) {
        PenaltyEntity penaltyEntity = penaltyRepository
                .findById(id)
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Income with id: " + id + " Not found."));

        PenaltyDto penaltyDto = modelMapper.map(penaltyEntity, PenaltyDto.class);

        UserEntity paymentMadeBy = userRepository
                .findById(penaltyEntity.getPaidBy()).orElse(null);

        assert paymentMadeBy != null;
        penaltyDto.setMadeBy(getPaidBy(paymentMadeBy));

        return Response.<PenaltyDto>builder()
                .data(penaltyDto)
                .code(HttpStatus.OK.value())
                .message("Income found.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }

    public Response<PaginatedResponse<PenaltyDto>> incomes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<PenaltyEntity> pages = penaltyRepository.findAll(pageable);

        return getPaginatedResponseResponse(page, size, pages);

    }


    public Response<PaginatedResponse<PenaltyDto>> getAllExpenses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        UUID userId = securityService.getCurrentUser().getId();

        Page<PenaltyEntity> pages = penaltyRepository.findAllMyExpenses(userId, pageable);

        return getPaginatedResponseResponse(page, size, pages);

    }

    private Response<PaginatedResponse<PenaltyDto>> getPaginatedResponseResponse(int page, int size, Page<PenaltyEntity> pages) {
        PaginatedResponse<PenaltyDto> response = new PaginatedResponse<>();
        response.setTotalElements(pages.getTotalElements());
        response.setTotalPages(pages.getTotalPages());
        response.setPage(page);
        response.setSize(size);
        response.setFirst(pages.isFirst());
        response.setLast(pages.isLast());
        response.setContent(pages.getContent().stream()
                .map(item -> {
                    PenaltyDto penaltyDto = modelMapper.map(item, PenaltyDto.class);
                    UserEntity paymentMadeBy = userRepository
                            .findById(item.getPaidBy()).orElse(null);

                    assert paymentMadeBy != null;
                    penaltyDto.setMadeBy(getPaidBy(paymentMadeBy));

                    return penaltyDto;
                })
                .toList()
        );
        return Response.<PaginatedResponse<PenaltyDto>>builder()
                .data(response)
                .code(HttpStatus.OK.value())
                .message("Incomes retrieved successfully.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }


    private PenaltyDto.PaidBy getPaidBy(UserEntity currentUser) {
        PenaltyDto.PaidBy paidBy = new PenaltyDto.PaidBy();
        paidBy.setId(currentUser.getId());
        paidBy.setFirstName(currentUser.getFirstName());
        paidBy.setLastName(currentUser.getLastName());

        return paidBy;
    }

}
