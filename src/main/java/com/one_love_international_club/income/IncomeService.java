package com.one_love_international_club.income;

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
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final SecurityService securityService;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final UserRepository userRepository;

    private static final String UPLOAD_PATH = "incomes";

    @Transactional
    public Response<IncomeDto> createIncome(IncomeDto incomeDto) {
        UserEntity currentUser = securityService.getCurrentUser();

        Map<String, String> uploaded = fileService
                .uploadBase64Image(incomeDto.getProofOfPayment(), UPLOAD_PATH);

        IncomeEntity incomeEntity = modelMapper.map(incomeDto, IncomeEntity.class);
        incomeEntity.setProofOfPayment(uploaded.get("fileUrl"));
        incomeEntity.setProofOfPaymentPublicId(uploaded.get("publicId"));
        incomeEntity.setPaidBy(currentUser.getId());

        IncomeEntity income = incomeRepository.save(incomeEntity);

        modelMapper.map(income, incomeDto);

        UserEntity paymentMadeBy = userRepository
                .findById(income.getPaidBy()).orElse(null);

        assert paymentMadeBy != null;
        incomeDto.setMadeBy(getPaidBy(paymentMadeBy));

        return Response.<IncomeDto>builder()
                .data(incomeDto)
                .code(HttpStatus.CREATED.value())
                .message("Income created.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }

    @Transactional
    public Response<IncomeDto> updateIncome(IncomeDto incomeDto) {
        IncomeEntity incomeEntity = incomeRepository
                .findById(incomeDto.getId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Income with id: " + incomeDto.getId() + " not found."));
        if (Objects.nonNull(incomeDto.getDue())) {
            incomeEntity.setDue(modelMapper.map(incomeDto.getDue(), DueEntity.class));
        }

        if (StringUtils.isNotBlank(incomeDto.getProofOfPayment()) && StringUtils.isNotBlank(incomeDto.getProofOfPaymentPublicId())) {
            fileService.deleteFile(incomeDto.getProofOfPaymentPublicId());
            Map<String, String> uploaded = fileService
                    .uploadBase64Image(incomeDto.getProofOfPayment(), UPLOAD_PATH);
            incomeEntity.setProofOfPayment(uploaded.get("fileUrl"));
            incomeEntity.setProofOfPaymentPublicId(uploaded.get("publicId"));
        }

        if (Objects.nonNull(incomeDto.getBank())) {
            incomeEntity.setBank(modelMapper.map(incomeDto.getBank(), BankEntity.class));
        }

        if (Objects.nonNull(incomeDto.getAmount())) {
            incomeEntity.setAmount(incomeDto.getAmount());
        }

        IncomeEntity save = incomeRepository.save(incomeEntity);
        modelMapper.map(save, incomeDto);

        UserEntity paymentMadeBy = userRepository
                .findById(save.getPaidBy()).orElse(null);

        assert paymentMadeBy != null;
        incomeDto.setMadeBy(getPaidBy(paymentMadeBy));

        return Response.<IncomeDto>builder()
                .data(incomeDto)
                .code(HttpStatus.OK.value())
                .message("Income updated.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();

    }

    public Response<IncomeDto> getIncomeById(UUID id) {
        IncomeEntity incomeEntity = incomeRepository
                .findById(id)
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Income with id: " + id + " Not found."));

        IncomeDto incomeDto = modelMapper.map(incomeEntity, IncomeDto.class);

        UserEntity paymentMadeBy = userRepository
                .findById(incomeEntity.getPaidBy()).orElse(null);

        assert paymentMadeBy != null;
        incomeDto.setMadeBy(getPaidBy(paymentMadeBy));

        return Response.<IncomeDto>builder()
                .data(incomeDto)
                .code(HttpStatus.OK.value())
                .message("Income found.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }

    public Response<PaginatedResponse<IncomeDto>> incomes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<IncomeEntity> pages = incomeRepository.findAll(pageable);

        return getPaginatedResponseResponse(page, size, pages);

    }


    public Response<PaginatedResponse<IncomeDto>> getAllExpenses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        UUID userId = securityService.getCurrentUser().getId();

        Page<IncomeEntity> pages = incomeRepository.findAllMyExpenses(userId, pageable);

        return getPaginatedResponseResponse(page, size, pages);

    }

    private Response<PaginatedResponse<IncomeDto>> getPaginatedResponseResponse(int page, int size, Page<IncomeEntity> pages) {
        PaginatedResponse<IncomeDto> response = new PaginatedResponse<>();
        response.setTotalElements(pages.getTotalElements());
        response.setTotalPages(pages.getTotalPages());
        response.setPage(page);
        response.setSize(size);
        response.setFirst(pages.isFirst());
        response.setLast(pages.isLast());
        response.setContent(pages.getContent().stream()
                .map(item -> {
                    IncomeDto incomeDto = modelMapper.map(item, IncomeDto.class);
                    UserEntity paymentMadeBy = userRepository
                            .findById(item.getPaidBy()).orElse(null);

                    assert paymentMadeBy != null;
                    incomeDto.setMadeBy(getPaidBy(paymentMadeBy));

                    return incomeDto;
                })
                .toList()
        );
        return Response.<PaginatedResponse<IncomeDto>>builder()
                .data(response)
                .code(HttpStatus.OK.value())
                .message("Incomes retrieved successfully.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }


    private IncomeDto.PaidBy getPaidBy(UserEntity currentUser) {
        IncomeDto.PaidBy paidBy = new IncomeDto.PaidBy();
        paidBy.setId(currentUser.getId());
        paidBy.setFirstName(currentUser.getFirstName());
        paidBy.setLastName(currentUser.getLastName());

        return paidBy;
    }

}
