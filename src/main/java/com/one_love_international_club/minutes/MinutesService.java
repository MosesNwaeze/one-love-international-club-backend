package com.one_love_international_club.minutes;

import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.meeting.MeetingEntity;
import com.one_love_international_club.meeting.MeetingRepository;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.Status;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
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
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MinutesService {

    private final MinutesRepository minutesRepository;
    private final ModelMapper modelMapper;
    private final MeetingRepository meetingRepository;


    @Transactional
    public Response<MinutesDto> publish(MinutesDto minutesDto) {

        MinutesEntity minutes = save(minutesDto);

        minutes.setDraft(Boolean.FALSE);

        MinutesEntity saved = minutesRepository.save(minutes);

        return Response.<MinutesDto>builder()
                .data(modelMapper.map(saved, MinutesDto.class))
                .code(HttpStatus.CREATED.value())
                .message("Successfully created minutes.")
                .timestamp(LocalDateTime.now())
                .build();
    }


    @Transactional
    public Response<MinutesDto> draft(MinutesDto minutesDto) {
        MinutesEntity minutes = save(minutesDto);

        minutes.setDraft(Boolean.TRUE);

        MinutesEntity saved = minutesRepository.save(minutes);

        return Response.<MinutesDto>builder()
                .data(modelMapper.map(saved, MinutesDto.class))
                .code(HttpStatus.CREATED.value())
                .message("Successfully drafted minutes.")
                .timestamp(LocalDateTime.now())
                .build();
    }


    @Transactional
    public Response<MinutesDto> update(MinutesDto minutesDto) {

        MinutesEntity minutesEntity = minutesRepository
                .findById(minutesDto.getId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Minutes with id " + minutesDto.getId() + " not found."));

        if (StringUtils.isNotBlank(minutesDto.getDiscussionSummary())) {
            minutesEntity.setDiscussionSummary(minutesDto.getDiscussionSummary());
        }

        if (StringUtils.isNotBlank(minutesDto.getResolution())) {
            minutesEntity.setResolution(minutesDto.getResolution());
        }

        if (Objects.nonNull(minutesDto.getMeeting())) {
            MeetingEntity meeting = modelMapper
                    .map(minutesDto.getMeeting(), MeetingEntity.class);
            meetingRepository.save(meeting);
        }

        minutesEntity.setUpdatedAt(LocalDateTime.now());

        MinutesEntity saved = minutesRepository.save(minutesEntity);

        return Response.<MinutesDto>builder()
                .data(modelMapper.map(saved, MinutesDto.class))
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .message("Successfully updated minutes.")
                .timestamp(LocalDateTime.now())
                .build();
    }


    public Response<MinutesDto> getMinutes(UUID id) {

        MinutesEntity minutesEntity = minutesRepository
                .findById(id)
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Minutes with id " + id + " not found."));

        return Response.<MinutesDto>builder()
                .data(modelMapper.map(minutesEntity, MinutesDto.class))
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .message("Successfully get minutes.")
                .timestamp(LocalDateTime.now())
                .build();
    }


    public Response<PaginatedResponse<MinutesDto>> getAllMinutes(int page, int size) {

        Pageable pageable = PageRequest
                .of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<MinutesEntity> pages = minutesRepository.findAll(pageable);

        PaginatedResponse<MinutesDto> response = new PaginatedResponse<>();

        response
                .setContent(pages.getContent().stream()
                        .map(item -> modelMapper.map(item, MinutesDto.class))
                        .toList());
        response.setPage(page);
        response.setSize(size);
        response.setFirst(pages.isFirst());
        response.setTotalElements(pages.getTotalElements());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());

        return Response.<PaginatedResponse<MinutesDto>>builder()
                .data(response)
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .message("Successfully get all minutes.")
                .timestamp(LocalDateTime.now())
                .build();
    }


    @Transactional
    public Response<Void> deleteMinutes(UUID id) {

        minutesRepository.deleteById(id);

        return Response.<Void>builder()
                .status(Status.SUCCESSFUL)
                .code(HttpStatus.OK.value())
                .message("Successfully deleted minutes.")
                .timestamp(LocalDateTime.now())
                .build();
    }


    private MinutesEntity save(MinutesDto minutesDto) {

        MeetingEntity meetingEntity = meetingRepository
                .findById(minutesDto.getMeeting().getId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Meeting with id " + minutesDto.getMeeting().getId() + " not found"));

        MinutesEntity minutes = modelMapper
                .map(minutesDto, MinutesEntity.class);

        minutes.setMeeting(meetingEntity);

        return minutes;

    }
}
