package com.one_love_international_club.benefits;

import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import com.one_love_international_club.util.StatusCodeResolver;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/benefits")
@Tag(name = "Benefit Controller", description = "A collection of endpoints for benefits")
@RequiredArgsConstructor
public class BenefitController {
    private final BenefitService benefitService;

    @PostMapping("/add-benefit")
    public ResponseEntity<Response<BenefitDto>> addBenefit(
            @RequestBody BenefitDto benefitDto
    ) {
        Response<BenefitDto> benefitDtoResponse = benefitService.addBenefit(benefitDto);
        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(benefitDtoResponse.getCode()))
                .body(benefitDtoResponse);
    }

    @GetMapping("/benefit-by-first-name-and-last-name")
    public ResponseEntity<Response<PaginatedResponse<BenefitDto>>> getBenefitForUser(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam("firstName") String  firstName,
            @RequestParam("lastName") String lastName
    ) {
        Response<PaginatedResponse<BenefitDto>> benefit = benefitService
                .getBenefit(firstName, lastName, page, size);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(benefit.getCode()))
                .body(benefit);
    }

    @GetMapping
    public ResponseEntity<Response<PaginatedResponse<BenefitDto>>> getAllBenefits(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        Response<PaginatedResponse<BenefitDto>> benefit = benefitService
                .getAllBenefits(page, size);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(benefit.getCode()))
                .body(benefit);
    }


    @PutMapping("/update-benefit")
    public ResponseEntity<Response<BenefitDto>> getAllBenefits(
            @RequestBody BenefitDto benefitDto
    ) {
        Response<BenefitDto> benefit = benefitService
                .updateBenefit(benefitDto);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(benefit.getCode()))
                .body(benefit);
    }

    @DeleteMapping("/{benefitId}")
    public ResponseEntity<Response<Void>> deleteBenefit(
            @PathVariable("benefitId")UUID benefitId
            ) {
        Response<Void> benefit = benefitService
                .deleteBenefit(benefitId);

        return ResponseEntity
                .status(StatusCodeResolver.getHttpStatus(benefit.getCode()))
                .body(benefit);
    }


}
