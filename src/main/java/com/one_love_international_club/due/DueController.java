package com.one_love_international_club.due;

import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.util.StatusCodeResolver;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/dues")
@Tag(name = "Due Controller", description = "Controller class for all dues related endpoints.")
@RequiredArgsConstructor
public class DueController {
    private final DueService dueService;

    @PostMapping
    @PreAuthorize("hasAnyRole('FINANCIAL SECRETARY', 'TREASURER', 'ADMIN')")
    public ResponseEntity<Response<DueDto>> createDue(
            @Valid @RequestBody DueDto dueDto
    ) {
        Response<DueDto> due = dueService.createDue(dueDto);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(due.getCode()))
                .body(due);
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('FINANCIAL SECRETARY', 'TREASURER', 'ADMIN')")
    public ResponseEntity<Response<DueDto>> updateDue(
            @RequestBody DueDto dueDto
    ) {
        Response<DueDto> due = dueService.updateDue(dueDto);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(due.getCode()))
                .body(due);
    }

    @GetMapping("/{dueId}")
    public ResponseEntity<Response<DueDto>> getDue(
            @PathVariable("dueId") UUID dueId
    ) {
        Response<DueDto> due = dueService.getDue(dueId);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(due.getCode()))
                .body(due);
    }

    @GetMapping
    public ResponseEntity<Response<List<DueDto>>> getAllDues() {
        Response<List<DueDto>> due = dueService.getAllDues();

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(due.getCode()))
                .body(due);
    }

    @DeleteMapping("/{dueId}")
    @PreAuthorize("hasAnyRole('FINANCIAL SECRETARY', 'TREASURER', 'ADMIN')")
    public ResponseEntity<Response<Void>> deleteDue(
            @PathVariable("dueId") UUID bankId
    ) {
        Response<Void> due = dueService.removeDue(bankId);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(due.getCode()))
                .body(due);
    }
}
