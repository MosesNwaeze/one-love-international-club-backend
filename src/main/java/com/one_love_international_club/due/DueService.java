package com.one_love_international_club.due;

import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DueService {
    private final DueRepository dueRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Response<DueDto> createDue(DueDto dueDto) {
        DueEntity dueEntity = modelMapper.map(dueDto, DueEntity.class);

        DueEntity save = dueRepository.save(dueEntity);

        log.info("Due created successfully, {}", save);

        return Response.<DueDto>builder()
                .code(HttpStatus.CREATED.value())
                .data(modelMapper.map(save, DueDto.class))
                .message("Due created successfully.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }

    @Transactional
    public Response<DueDto> updateDue(DueDto dueDto) {
        DueEntity dueEntity = dueRepository
                .findById(dueDto.getId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Due with id: " + dueDto.getId() + " not found"));

        dueEntity.setName(dueDto.getName());

        DueEntity save = dueRepository.save(dueEntity);

        log.info("Due updated successfully, {}", save);

        return Response.<DueDto>builder()
                .code(HttpStatus.CREATED.value())
                .data(modelMapper.map(save, DueDto.class))
                .message("Due update successfully.")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }

    public Response<DueDto> getDue(UUID id) {
        DueEntity dueEntity = dueRepository
                .findById(id)
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Due with id " + id + " not found"));

        return Response.<DueDto>builder()
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .data(modelMapper.map(dueEntity, DueDto.class))
                .timestamp(LocalDateTime.now())
                .build();
    }

    public Response<List<DueDto>> getAllDues() {
        List<DueDto> dueDtos = dueRepository
                .findAll()
                .stream()
                .map(due -> modelMapper.map(due, DueDto.class))
                .toList();

        return Response.<List<DueDto>>builder()
                .timestamp(LocalDateTime.now())
                .data(dueDtos)
                .code(HttpStatus.OK.value())
                .status(Status.SUCCESSFUL)
                .message("Due retrieved successfully.")
                .build();
    }

    @Transactional
    public Response<Void> removeDue(UUID id) {
        dueRepository.deleteById(id);

        log.info("due with id {}, deleted successfully", id);

        return Response.<Void>builder()
                .message("Due deleted successfully")
                .timestamp(LocalDateTime.now())
                .status(Status.SUCCESSFUL)
                .build();
    }
}
