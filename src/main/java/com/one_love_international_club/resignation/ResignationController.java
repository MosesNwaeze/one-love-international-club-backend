package com.one_love_international_club.resignation;

import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import com.one_love_international_club.util.StatusCodeResolver;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/resignations")
@Tag(name = "Resignation Controller", description = "A controller that manages everything about resignation.")
@RequiredArgsConstructor
public class ResignationController {
    private final ResignationService resignationService;

    @PostMapping("/resign")
    public ResponseEntity<Response<ResignationDto>> resign(
            @Valid @RequestBody ResignationDto resignationDto
    ) {
        Response<ResignationDto> resign = resignationService.resign(resignationDto);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(resign.getCode()))
                .body(resign);
    }


    @PatchMapping
    public ResponseEntity<Response<ResignationDto>> approve(
            @Valid @RequestBody ApproveRejectDto resignationDto
    ) {
        Response<ResignationDto> approve = resignationService
                .approve(resignationDto.getResignationId(), resignationDto.getRejectionReason(), resignationDto.getStatus());

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(approve.getCode()))
                .body(approve);
    }


    @GetMapping
    public ResponseEntity<Response<PaginatedResponse<ResignationDto>>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        Response<PaginatedResponse<ResignationDto>> allResignation = resignationService.getAllResignation(page, size);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(allResignation.getCode()))
                .body(allResignation);
    }
}
