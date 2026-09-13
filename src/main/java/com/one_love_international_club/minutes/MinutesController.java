package com.one_love_international_club.minutes;

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
@RequestMapping("/v1/minutes")
@Tag(name = "Minutes Controller",
        description = "Controller class for all minutes related endpoints.")
@RequiredArgsConstructor
public class MinutesController {

    private final MinutesService minutesService;

    @PreAuthorize("hasRole('SECRETARY')")
    @PostMapping("/publish")
    public ResponseEntity<Response<MinutesDto>> publishMeeting(
            @Valid @RequestBody MinutesDto meetingDto) {
        Response<MinutesDto> response = minutesService.publish(meetingDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @PreAuthorize("hasRole('SECRETARY')")
    @PostMapping("/save-as-draft")
    public ResponseEntity<Response<MinutesDto>> draft(
            @Valid @RequestBody MinutesDto meetingDto) {
        Response<MinutesDto> response = minutesService.draft(meetingDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @PreAuthorize("hasRole('SECRETARY')")
    @PatchMapping("/update")
    public ResponseEntity<Response<MinutesDto>> update(
            @Valid @RequestBody MinutesDto meetingDto) {
        Response<MinutesDto> response = minutesService.update(meetingDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @GetMapping("/{minuteId}")
    public ResponseEntity<Response<MinutesDto>> getMinutes(
            @PathVariable("minuteId") UUID minuteId) {
        Response<MinutesDto> response = minutesService.getMinutes(minuteId);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @GetMapping()
    public ResponseEntity<Response<PaginatedResponse<MinutesDto>>> getAllMinutes(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Response<PaginatedResponse<MinutesDto>> response = minutesService.getAllMinutes(page, size);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }

    @PreAuthorize("hasRole('SECRETARY')")
    @DeleteMapping("/{minutesId}")
    public ResponseEntity<Response<Void>> deleteMinutes(
            @PathVariable("minutesId") UUID meetingId
    ) {

        Response<Void> response = minutesService.deleteMinutes(meetingId);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);
    }
}
