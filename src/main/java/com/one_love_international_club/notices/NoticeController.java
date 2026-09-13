package com.one_love_international_club.notices;

import com.one_love_international_club.enums.Audience;
import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import com.one_love_international_club.util.StatusCodeResolver;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/notices")
@Tag(name = "Notices Controller",
        description = "Controller class for all notice related endpoints.")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;


    public ResponseEntity<Response<PaginatedResponse<NoticeDto>>> getAllNotices(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {

        Response<PaginatedResponse<NoticeDto>> response = noticeService.getAllNotices(page, size);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @GetMapping("/audience")
    public ResponseEntity<Response<PaginatedResponse<NoticeDto>>> getAllNotices(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "audience", required = false) Audience audience
    ) {

        Response<PaginatedResponse<NoticeDto>> response = noticeService.findAllNoticeByAudience(page, size, audience);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }


    @GetMapping("/{noticeId}")
    public ResponseEntity<Response<NoticeDto>> getNotice(
            @PathVariable("noticeId") UUID noticeId
    ) {

        Response<NoticeDto> response = noticeService.getNotice(noticeId);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @PreAuthorize("hasRole('SECRETARY')")
    @PostMapping("/publish-notice")
    public ResponseEntity<Response<NoticeDto>> publishNotice(
            @Valid @RequestBody NoticeDto noticeDto
    ) {

        Response<NoticeDto> response = noticeService.publish(noticeDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }


    @PreAuthorize("hasRole('SECRETARY')")
    @PostMapping("/draft-notice")
    public ResponseEntity<Response<NoticeDto>> draftNotice(
            @Valid @RequestBody NoticeDto noticeDto
    ) {

        Response<NoticeDto> response = noticeService.draft(noticeDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }


    @PreAuthorize("hasRole('SECRETARY')")
    @PatchMapping
    public ResponseEntity<Response<NoticeDto>> updateNotice(
            @Valid @RequestBody NoticeDto noticeDto
    ) {

        Response<NoticeDto> response = noticeService.updateNotice(noticeDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @PreAuthorize("hasRole('SECRETARY')")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Response<Void>> removeNotice(
            @PathVariable("noticeId") UUID noticeId
    ) {

        Response<Void> response = noticeService.deleteNotice(noticeId);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }
}
