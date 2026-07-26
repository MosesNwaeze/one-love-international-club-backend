package com.one_love_international_club.committee;

import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import com.one_love_international_club.util.StatusCodeResolver;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/committees")
@RequiredArgsConstructor
@Tag(name = "Committee Controller.", description = "A controller that manages all endpoints related to committee.")
public class CommitteeController {

    private final CommitteeService committeeService;

    @PostMapping
    public ResponseEntity<Response<CommitteeDto>> createCommittee(
            @Valid @RequestBody CommitteeDto committeeDto
    ) {
        Response<CommitteeDto> committee = committeeService.createCommittee(committeeDto);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(committee.getCode())).body(committee);
    }

    @PatchMapping
    public ResponseEntity<Response<CommitteeDto>> updateCommittee(
            @RequestBody CommitteeDto committeeDto
    ) {
        Response<CommitteeDto> committeeDtoResponse = committeeService.updateCommittee(committeeDto);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(committeeDtoResponse.getCode()))
                .body(committeeDtoResponse);
    }


    @PatchMapping("/generate-report")
    public ResponseEntity<Response<CommitteeDto>> generateReport(
            @RequestBody CommitteeReportDto reportDto
    ) {
        Response<CommitteeDto> committeeDtoResponse = committeeService
                .generateReport(reportDto.getCommitteeId(), reportDto.getReport());

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(committeeDtoResponse.getCode()))
                .body(committeeDtoResponse);
    }


    @DeleteMapping("/delete-report/{committeeId}")
    public ResponseEntity<Response<Void>> removeReport(
            @PathVariable("committeeId") UUID committeeId
    ) {
        Response<Void> committeeDtoResponse = committeeService
                .remove(committeeId);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(committeeDtoResponse.getCode()))
                .body(committeeDtoResponse);
    }

    @GetMapping("/get-committee")
    public ResponseEntity<Response<CommitteeDto>> getCommittee(CommitteeDto committeeDto) {

        Response<CommitteeDto> committee = committeeService
                .getCommitteeById(committeeDto.getId());

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(committee.getCode()))
                .body(committee);
    }


    @GetMapping
    public ResponseEntity<Response<PaginatedResponse<CommitteeDto>>> getAllCommittee(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {

        Response<PaginatedResponse<CommitteeDto>> committee = committeeService
                .getAllCommittee(page, size);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(committee.getCode()))
                .body(committee);
    }
}
