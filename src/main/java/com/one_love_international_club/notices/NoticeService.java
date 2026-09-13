package com.one_love_international_club.notices;

import com.one_love_international_club.enums.Audience;
import com.one_love_international_club.exception.ClubException;
import com.one_love_international_club.exception.ErrorCode;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Response<NoticeDto> publish(NoticeDto noticeDto) {

        NoticeEntity noticeEntity = saveNotice(noticeDto);

        noticeEntity.setSaveAsDraft(false);

        NoticeEntity save = noticeRepository.save(noticeEntity);

        return Response.<NoticeDto>builder()
                .data(modelMapper.map(save, NoticeDto.class))
                .message("Notice published successfully.")
                .build();


    }

    @Transactional
    public Response<NoticeDto> draft(NoticeDto noticeDto) {

        NoticeEntity noticeEntity = saveNotice(noticeDto);

        noticeEntity.setSaveAsDraft(true);

        NoticeEntity save = noticeRepository.save(noticeEntity);

        return Response.<NoticeDto>builder()
                .data(modelMapper.map(save, NoticeDto.class))
                .message("Notice drafted successfully.")
                .build();


    }

    @Transactional
    public Response<NoticeDto> updateNotice(NoticeDto noticeDto) {

        NoticeEntity noticeEntity = modelMapper.map(noticeDto, NoticeEntity.class);
        noticeEntity.setUpdatedAt(LocalDateTime.now());

        NoticeEntity save = noticeRepository.save(noticeEntity);

        return Response.<NoticeDto>builder()
                .data(modelMapper.map(save, NoticeDto.class))
                .message("Notice updated successfully.")
                .timestamp(LocalDateTime.now())
                .code(200)
                .build();

    }

    public Response<NoticeDto> getNotice(UUID id) {

        NoticeEntity noticeEntity = noticeRepository
                .findById(id)
                .orElseThrow(() -> new ClubException(ErrorCode.ENTITY_NOT_FOUND,
                        "Notice with id: " + id + " not found"));

        return Response.<NoticeDto>builder()
                .data(modelMapper.map(noticeEntity, NoticeDto.class))
                .message("Notice retrieved successfully.")
                .timestamp(LocalDateTime.now())
                .code(200)
                .build();
    }


    public Response<PaginatedResponse<NoticeDto>> getAllNotices(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<NoticeEntity> pages = noticeRepository.findAll(pageable);

        return getPaginatedResponseResponse(page, size, pages);

    }


    public Response<PaginatedResponse<NoticeDto>> findAllNoticeByAudience(int page, int size, Audience audience) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<NoticeEntity> pages = noticeRepository.findAllNoticesByAudience(audience, pageable);

        return getPaginatedResponseResponse(page, size, pages);

    }

    private Response<PaginatedResponse<NoticeDto>> getPaginatedResponseResponse(int page, int size, Page<NoticeEntity> pages) {
        PaginatedResponse<NoticeDto> response = new PaginatedResponse<>();

        response.setLast(pages.isLast());
        response.setFirst(pages.isFirst());
        response.setSize(size);
        response.setTotalElements(pages.getTotalElements());
        response.setContent(
                pages.getContent().stream()
                        .map(item -> modelMapper.map(item, NoticeDto.class))
                        .toList()
        );
        response.setPage(page);
        response.setTotalPages(pages.getTotalPages());

        return Response.<PaginatedResponse<NoticeDto>>builder()
                .data(response)
                .message("Notices retrieved successfully.")
                .timestamp(LocalDateTime.now())
                .code(200)
                .build();
    }


    @Transactional
    public Response<Void> deleteNotice(UUID id) {

        noticeRepository.deleteById(id);

        return Response.<Void>builder()
                .code(200)
                .message("Notice deleted successfully.")
                .timestamp(LocalDateTime.now())
                .build();
    }


    private NoticeEntity saveNotice(NoticeDto noticeDto) {
        return modelMapper.map(noticeDto, NoticeEntity.class);
    }
}
