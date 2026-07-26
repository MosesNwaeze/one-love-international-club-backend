package com.one_love_international_club.expenses;

import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.auth.repo.UserRepository;
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
@Slf4j
@RequiredArgsConstructor
public class ExpensesService {
    private final ExpensesRepository expensesRepository;
    private final ModelMapper modelMapper;
    private final SecurityService securityService;
    private final FileService fileService;
    private final UserRepository userRepository;

    private static final String UPLOAD_PATH = "expenses";

    @Transactional
    public Response<ExpensesDto> createExpenses(ExpensesDto expensesDto) {

        ExpensesEntity expenseEntity = modelMapper.map(expensesDto, ExpensesEntity.class);

        Map<String, String> uploaded = fileService
                .uploadBase64Image(expensesDto.getProofOfPayment(), UPLOAD_PATH);

        expenseEntity.setProofOfPayment(uploaded.get("fileUrl"));
        expenseEntity.setProofOfPaymentPublicId(uploaded.get("publicId"));
        expenseEntity = expensesRepository.save(expenseEntity);

        modelMapper.map(expenseEntity, expensesDto);

        expensesDto.setPaymentMadeBy(getDetails(expenseEntity.getPaidBy()));
        expensesDto.setPaymentReceivedBy(getDetails(expenseEntity.getReceiver()));

        return Response.<ExpensesDto>builder()
                .message("Expenses created.")
                .data(expensesDto)
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();

    }


    @Transactional
    public Response<ExpensesDto> updateExpense(ExpensesDto expensesDto) {
        ExpensesEntity expenses = expensesRepository
                .findById(expensesDto.getId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Expenses with id " + expensesDto.getId() + " not found."));

        if (Objects.nonNull(expensesDto.getAmount())) {
            expenses.setAmount(expensesDto.getAmount());
        }

        if (Objects.nonNull(expensesDto.getType())) {
            expenses.setType(expensesDto.getType());
        }

        if (StringUtils.isNotBlank(expensesDto.getProofOfPayment()) &&
                StringUtils.isNotBlank(expensesDto.getProofOfPaymentPublicId())) {
            fileService.deleteFile(expenses.getProofOfPaymentPublicId());

            Map<String, String> uploaded = fileService
                    .uploadBase64Image(expensesDto.getProofOfPayment(), UPLOAD_PATH);
            expenses.setProofOfPayment(uploaded.get("fileUrl"));
            expenses.setProofOfPaymentPublicId(uploaded.get("publicId"));
        }

        expenses = expensesRepository.save(expenses);

        modelMapper.map(expenses, expensesDto);

        expensesDto.setPaymentMadeBy(getDetails(expenses.getPaidBy()));
        expensesDto.setPaymentReceivedBy(getDetails(expenses.getReceiver()));

        return Response.<ExpensesDto>builder()
                .timestamp(LocalDateTime.now())
                .message("Expenses updated successfully.")
                .code(HttpStatus.OK.value())
                .status(Status.SUCCESSFUL)
                .data(expensesDto)
                .build();
    }


    public Response<PaginatedResponse<ExpensesDto>> getAllExpenses(int page, int size) {
        Pageable pageable = PageRequest
                .of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<ExpensesEntity> all = expensesRepository.findAll(pageable);

        return getPaginatedResponseResponse(page, size, all);

    }


    public Response<PaginatedResponse<ExpensesDto>> getAllMyBenefits(int page, int size) {
        Pageable pageable = PageRequest
                .of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        UUID userId = securityService.getCurrentUser().getId();

        Page<ExpensesEntity> all = expensesRepository.findAllMyBenefits(userId, pageable);

        return getPaginatedResponseResponse(page, size, all);
    }

    private Response<PaginatedResponse<ExpensesDto>> getPaginatedResponseResponse(int page, int size, Page<ExpensesEntity> all) {
        PaginatedResponse<ExpensesDto> response = new PaginatedResponse<>();
        response.setPage(page);
        response.setSize(size);
        response.setContent(all.getContent().stream().map(item -> {
            ExpensesDto expensesDto = modelMapper.map(item, ExpensesDto.class);
            expensesDto.setPaymentReceivedBy(getDetails(item.getReceiver()));
            expensesDto.setPaymentMadeBy(getDetails(item.getPaidBy()));
            return expensesDto;
        }).toList());
        response.setTotalElements(all.getTotalElements());
        response.setTotalPages(all.getTotalPages());
        response.setLast(all.isLast());
        response.setFirst(all.isFirst());

        return Response.<PaginatedResponse<ExpensesDto>>builder()
                .data(response)
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private ExpensesDto.Details getDetails(UUID userId) {
        ExpensesDto.Details details = new ExpensesDto.Details();
        UserEntity user = userRepository.findById(userId).orElse(null);

        assert user != null;

        details.setFirstName(user.getFirstName());
        details.setLastName(user.getLastName());
        details.setId(user.getId());

        return details;
    }
}
