package com.one_love_international_club.file_upload;

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
@RequestMapping("/v1/files")
@Tag(name = "Upload Controller", description = "Controller class for all upload related endpoints.")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/upload")
    public ResponseEntity<Response<UploadDto>> uploadFile(
            @Valid @RequestBody UploadDto uploadDto
    ) {

        Response<UploadDto> response = uploadService.upload(uploadDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);

    }

    @GetMapping("/{uploadId}")
    public ResponseEntity<Response<UploadDto>> getUploadFile(
            @PathVariable("uploadId") UUID uploadId
    ) {

        Response<UploadDto> response = uploadService.getUpload(uploadId);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);

    }

    @PreAuthorize("hasRole('EXECUTIVE')")
    @GetMapping()
    public ResponseEntity<Response<PaginatedResponse<UploadDto>>> getAllUploadFiles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {

        Response<PaginatedResponse<UploadDto>> response = uploadService.getAllUploads(page, size);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(response.getCode()))
                .body(response);

    }
}
