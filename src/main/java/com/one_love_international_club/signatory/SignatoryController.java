package com.one_love_international_club.signatory;

import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.util.StatusCodeResolver;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/signatories")
@Tag(name = "Signatory Controller", description = "Controller that manages all the endpoint regarding signatory.")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('Treasurer', 'Financial Secretary', 'President')")
public class SignatoryController {

    private final SignatoryService signatoryService;

    @PostMapping
    public ResponseEntity<Response<SignatoryDto>> createSignatory(
            @RequestBody SignatoryDto signatoryDto
    ) {
        Response<SignatoryDto> signatory = signatoryService.createSignatory(signatoryDto);
        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(signatory.getCode()))
                .body(signatory);
    }

    @PutMapping
    public ResponseEntity<Response<SignatoryDto>> updateSignatory(
            @RequestBody SignatoryDto signatoryDto
    ) {
        Response<SignatoryDto> signatory = signatoryService.updateSignatory(signatoryDto);
        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(signatory.getCode()))
                .body(signatory);
    }

    @GetMapping("/{signatoryId}")
    public ResponseEntity<Response<SignatoryDto>> getSignatory(
            @PathVariable("signatoryId") UUID signatoryId
    ) {
        Response<SignatoryDto> signatory = signatoryService.getSignatory(signatoryId);
        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(signatory.getCode()))
                .body(signatory);
    }

    @GetMapping
    public ResponseEntity<Response<List<SignatoryDto>>> getSignatories() {
        Response<List<SignatoryDto>> signatory = signatoryService.getSignatories();
        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(signatory.getCode()))
                .body(signatory);
    }

    @DeleteMapping("/{signatoryId}")
    public ResponseEntity<Response<Void>> removeSignatory(
            @PathVariable("signatoryId") UUID signatoryId
    ) {
        Response<Void> signatory = signatoryService.removeSignatory(signatoryId);
        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(signatory.getCode()))
                .body(signatory);
    }
}
