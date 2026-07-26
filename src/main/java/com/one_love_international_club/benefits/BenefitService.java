package com.one_love_international_club.benefits;

import com.one_love_international_club.auth.dto.UserDto;
import com.one_love_international_club.auth.entity.UserEntity;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.Status;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
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
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BenefitService {
    private final BenefitRepository benefitRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Response<BenefitDto> addBenefit(BenefitDto benefitDto) {

        BenefitEntity benefitEntity = modelMapper.map(benefitDto, BenefitEntity.class);

        BenefitEntity save = benefitRepository.save(benefitEntity);

        modelMapper.map(save, benefitDto);

        return Response.<BenefitDto>builder()
                .code(HttpStatus.CREATED.value())
                .message("Benefit added successfully.")
                .timestamp(LocalDateTime.now())
                .data(benefitDto)
                .status(Status.SUCCESSFUL)
                .build();
    }

    public Response<PaginatedResponse<BenefitDto>> getBenefit(String firstName, String lastName, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<BenefitEntity> pages = benefitRepository
                .findAllByUser(firstName, lastName, pageable);


        PaginatedResponse<BenefitDto> response = new PaginatedResponse<>();
        response.setContent(pages.getContent().stream().map(benefitEntity -> modelMapper.map(benefitEntity, BenefitDto.class))
                .toList());
        response.setPage(page);
        response.setSize(size);
        response.setTotalPages(pages.getTotalPages());
        response.setTotalElements(pages.getTotalElements());

        return Response.<PaginatedResponse<BenefitDto>>builder()
                .data(response)
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("Benefits found")
                .status(Status.SUCCESSFUL)
                .build();

    }

    public Response<PaginatedResponse<BenefitDto>> getAllBenefits(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BenefitEntity> pages = benefitRepository.findAll(pageable);

        PaginatedResponse<BenefitDto> response = new PaginatedResponse<>();
        response.setContent(pages.getContent().stream()
                .map(benefitEntity -> modelMapper.map(benefitEntity, BenefitDto.class)).toList());
        response.setPage(page);
        response.setSize(size);
        response.setTotalPages(pages.getTotalPages());
        response.setTotalElements(pages.getTotalElements());

        return Response.<PaginatedResponse<BenefitDto>>builder()
                .status(Status.SUCCESSFUL)
                .message("All benefits retrieved successfully.")
                .code(HttpStatus.OK.value())
                .data(response)
                .build();
    }

    @Transactional
    public Response<BenefitDto> updateBenefit(BenefitDto benefitDto) {

        BenefitEntity benefitEntity = benefitRepository
                .findById(benefitDto.getId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "benefit with id: " + benefitDto.getId() + " Not found"));

        if (Objects.nonNull(benefitDto.getUser())) {
            benefitEntity.setUser(modelMapper.map(benefitDto.getUser(), UserEntity.class));
        }

        if (Objects.nonNull(benefitDto.getAmountReceived())) {
            benefitEntity.setAmountReceived(benefitDto.getAmountReceived());
        }

        BenefitEntity save = benefitRepository.save(benefitEntity);

        log.info("Benefit updated successfully, {}", save);

        return Response.<BenefitDto>builder()
                .code(HttpStatus.OK.value())
                .message("Benefit updated successfully.")
                .timestamp(LocalDateTime.now())
                .data(modelMapper.map(save, BenefitDto.class))
                .status(Status.SUCCESSFUL)
                .build();

    }


    @Transactional
    public Response<Void> deleteBenefit(UUID benefitId) {
        benefitRepository.deleteById(benefitId);
        return Response.<Void>builder()
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .message("Benefit deleted successfully")
                .build();
    }

}
