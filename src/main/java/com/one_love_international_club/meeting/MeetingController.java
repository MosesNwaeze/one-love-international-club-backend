package com.one_love_international_club.meeting;

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
@RequestMapping("/v1/meeting")
@Tag(name = "Meeting Controller",
        description = "Controller class for all meetings related endpoints.")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PreAuthorize("hasRole('SECRETARY')")
    @PostMapping("/publish")
    public ResponseEntity<Response<MeetingDto>> publishMeeting(
            @Valid @RequestBody MeetingDto meetingDto) {
        Response<MeetingDto> response = meetingService.publishMeeting(meetingDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @PreAuthorize("hasRole('SECRETARY')")
    @PostMapping("/save-as-draft")
    public ResponseEntity<Response<MeetingDto>> draft(
            @Valid @RequestBody MeetingDto meetingDto) {
        Response<MeetingDto> response = meetingService.saveAsDraft(meetingDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @PreAuthorize("hasRole('SECRETARY')")
    @PatchMapping("/update")
    public ResponseEntity<Response<MeetingDto>> update(
            @Valid @RequestBody MeetingDto meetingDto) {
        Response<MeetingDto> response = meetingService.updateMeeting(meetingDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<Response<MeetingDto>> getMeeting(
            @PathVariable("meetingId") UUID meetingId) {
        Response<MeetingDto> response = meetingService.getMeetingById(meetingId);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Response<PaginatedResponse<MeetingDto>>> getMeetings(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Response<PaginatedResponse<MeetingDto>> response = meetingService.getAllMeetings(page, size);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @PreAuthorize("hasRole('SECRETARY')")
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<Response<Void>> deleteMeeting(
            @PathVariable("meetingId") UUID meetingId
    ) {

        Response<Void> response = meetingService.deleteMeeting(meetingId);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }
}
