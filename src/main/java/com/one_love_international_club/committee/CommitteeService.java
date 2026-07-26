package com.one_love_international_club.committee;

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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class CommitteeService {

    private final CommitteeRepository committeeRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Response<CommitteeDto> createCommittee(CommitteeDto committeeDto) {

        CommitteeEntity committeeEntity = modelMapper.map(committeeDto, CommitteeEntity.class);


        if (committeeDto.getTotalMembersAllowed() != committeeDto.getMember().size()) {
            throw new ClubException(ErrorCode.VALIDATION_ERROR,
                    "Total number of members does not match actual members entered.");
        }

        List<UserEntity> committeeMembers = committeeDto
                .getMember()
                .stream()
                .map(mem -> modelMapper.map(mem, UserEntity.class)).toList();

        committeeEntity.getMember().addAll(committeeMembers);

        CommitteeEntity saved = committeeRepository.save(committeeEntity);

        log.info("Committee saved successfully. {}", saved);

        return Response.<CommitteeDto>builder()
                .message("Committee saved successfully.")
                .code(HttpStatus.CREATED.value())
                .data(modelMapper.map(saved, CommitteeDto.class))
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }


    public Response<CommitteeDto> getCommitteeById(UUID id) {
        CommitteeEntity committeeEntity = committeeRepository
                .findById(id).orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Committee with id: " + id + " not found."));

        log.info("Committee with id: {}, retrieved successfully {}", id, committeeEntity);

        return Response.<CommitteeDto>builder()
                .status(Status.SUCCESSFUL)
                .message("Committee found.")
                .data(modelMapper.map(committeeEntity, CommitteeDto.class))
                .timestamp(LocalDateTime.now())
                .build();

    }

    public Response<PaginatedResponse<CommitteeDto>> getAllCommittee(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<CommitteeEntity> pages = committeeRepository.findAll(pageable);

        PaginatedResponse<CommitteeDto> response = new PaginatedResponse<>();
        response.setContent(pages.getContent()
                .stream().map(item -> modelMapper.map(item, CommitteeDto.class)).toList());
        response.setPage(page);
        response.setSize(size);
        response.setFirst(pages.isFirst());
        response.setLast(pages.isLast());
        response.setTotalElements(pages.getTotalElements());

        log.info("Committee retrieved successfully {}", pages.getContent());

        return Response.<PaginatedResponse<CommitteeDto>>builder()
                .status(Status.SUCCESSFUL)
                .message("Committee found.")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();

    }

    @Transactional
    public Response<CommitteeDto> updateCommittee(CommitteeDto committeeDto) {
        CommitteeEntity committeeEntity = committeeRepository
                .findById(committeeDto.getId()).orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Committee with id: " + committeeDto.getId() + " not found."));

        if (Objects.nonNull(committeeDto.getName())) {
            committeeEntity.setName(committeeDto.getName());
        }

        if (Objects.nonNull(committeeDto.getDescription())) {
            committeeEntity.setDescription(committeeDto.getDescription());
        }

        if (Objects.nonNull(committeeDto.getTotalMembersAllowed())) {
            committeeEntity.setTotalMembersAllowed(committeeDto.getTotalMembersAllowed());
        }

        if (Objects.nonNull(committeeDto.getMember()) && committeeDto.getMember().size() == committeeDto.getTotalMembersAllowed()) {
            committeeEntity.getMember().clear();
            List<UserEntity> members = committeeDto.getMember()
                    .stream().map(mem -> modelMapper.map(mem, UserEntity.class)).toList();

            committeeEntity.getMember().addAll(members);
        }

        CommitteeEntity save = committeeRepository.save(committeeEntity);

        return Response.<CommitteeDto>builder()
                .message("Committee updated successfully.")
                .code(HttpStatus.OK.value())
                .data(modelMapper.map(save, CommitteeDto.class))
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();


    }

    @Transactional
    public Response<CommitteeDto> generateReport(UUID committeeId, String report) {
        CommitteeEntity committeeEntity = committeeRepository
                .findById(committeeId).orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Committee with id: " + committeeId + " not found."));

        committeeEntity.setResolutionReport(report);

        CommitteeEntity save = committeeRepository.save(committeeEntity);

        return Response.<CommitteeDto>builder()
                .message("Committee report generated successfully.")
                .code(HttpStatus.OK.value())
                .status(Status.SUCCESSFUL)
                .data(modelMapper.map(save, CommitteeDto.class))
                .timestamp(LocalDateTime.now())
                .build();

    }

    @Transactional
    public Response<Void> remove(UUID committeeId) {
        committeeRepository.deleteById(committeeId);

        log.info("Committee deleted successfully.");

        return Response.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Committee deleted successfully.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }
}
