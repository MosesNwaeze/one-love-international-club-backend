package com.one_love_international_club.meeting;

import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
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
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    private final static String UPLOAD_PATH = "meetings";

    @Transactional
    public Response<MeetingDto> publishMeeting(MeetingDto meetingDto) {

        MeetingEntity meetingEntity = createMeeting(meetingDto);
        meetingEntity.setSaveAsDraft(Boolean.FALSE);
        MeetingEntity save = meetingRepository.save(meetingEntity);

        modelMapper.map(save, meetingDto);

        return Response.<MeetingDto>builder()
                .data(meetingDto)
                .message("Meeting published successfully")
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.CREATED.value())
                .build();
    }

    @Transactional
    public Response<MeetingDto> saveAsDraft(MeetingDto meetingDto) {

        MeetingEntity meetingEntity = createMeeting(meetingDto);
        meetingEntity.setSaveAsDraft(Boolean.TRUE);
        MeetingEntity save = meetingRepository.save(meetingEntity);

        modelMapper.map(save, meetingDto);

        return Response.<MeetingDto>builder()
                .data(meetingDto)
                .message("Meeting saved as draft successfully")
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.CREATED.value())
                .build();
    }


    @Transactional
    public Response<MeetingDto> updateMeeting(MeetingDto meetingDto) {
        MeetingEntity meeting = meetingRepository
                .findById(meetingDto.getId())
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Meeting with id" + meetingDto.getId() + " not found."));

        if (StringUtils.isNotBlank(meetingDto.getDocuments()) &&
                StringUtils.isNotBlank(meetingDto.getDocumentPublicId())) {
            uploadFile(meetingDto, meeting);
        }

        if (StringUtils.isNotBlank(meetingDto.getMeetingTitle())) {
            meeting.setMeetingTitle(meetingDto.getMeetingTitle());
        }

        if (StringUtils.isNotBlank(meetingDto.getAgender())) {
            meeting.setAgender(meetingDto.getAgender());
        }

        if (StringUtils.isNotBlank(meetingDto.getVenue())) {
            meeting.setVenue(meetingDto.getVenue());
        }

        if (Objects.nonNull(meetingDto.getDate())) {
            meeting.setDate(meetingDto.getDate());
        }

        meeting.setUpdatedAt(LocalDateTime.now());
        MeetingEntity save = meetingRepository.save(meeting);

        return Response.<MeetingDto>builder()
                .data(modelMapper.map(save, MeetingDto.class))
                .message("Meeting updated successfully")
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.OK.value())
                .build();

    }

    public Response<MeetingDto> getMeetingById(UUID meetingId) {

        MeetingEntity meetingEntity = meetingRepository
                .findById(meetingId)
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Meeting with id" + meetingId + " not found."));

        return Response.<MeetingDto>builder()
                .data(modelMapper.map(meetingEntity, MeetingDto.class))
                .message("Meeting get successfully")
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.OK.value())
                .build();
    }


    public Response<PaginatedResponse<MeetingDto>> getAllMeetings(int page, int size) {

        Pageable pageable = PageRequest
                .of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<MeetingEntity> pages = meetingRepository.findAll(pageable);

        PaginatedResponse<MeetingDto> response = new PaginatedResponse<>();
        response.setFirst(pages.isFirst());
        response.setTotalElements(pages.getTotalElements());
        response.setTotalPages(pages.getTotalPages());
        response.setPage(page);
        response.setSize(size);
        response.setLast(pages.isLast());
        response.setContent(pages.getContent().stream()
                .map(item -> modelMapper.map(item, MeetingDto.class))
                .toList()
        );

        return Response.<PaginatedResponse<MeetingDto>>builder()
                .status(Status.SUCCESSFUL)
                .message("Meetings return successfully.")
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.OK.value())
                .build();

    }


    @Transactional
    public Response<Void> deleteMeeting(UUID meetingId) {

        meetingRepository.deleteById(meetingId);

        return Response.<Void>builder()
                .message("Meeting deleted successfully")
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.NO_CONTENT.value())
                .build();
    }

    private void uploadFile(MeetingDto meetingDto, MeetingEntity meeting) {

        if (StringUtils.isNotBlank(meetingDto.getDocuments())) {
            Map<String, String> uploaded = fileService
                    .uploadBase64Image(meetingDto.getDocuments(), UPLOAD_PATH);

            meeting.setDocuments(uploaded.get("fileUrl"));
            meeting.setDocumentPublicId(uploaded.get("publicId"));

        }

    }

    private MeetingEntity createMeeting(MeetingDto meetingDto) {

        MeetingEntity meeting = modelMapper
                .map(meetingDto, MeetingEntity.class);

        uploadFile(meetingDto, meeting);

        return meeting;

    }

}
