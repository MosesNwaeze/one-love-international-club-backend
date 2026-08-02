package com.one_love_international_club.penalty;

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
@RequestMapping("/v1/penalties")
@Tag(name = "Penalty Controller", description = "Controller class for all penalty related endpoints.")
@RequiredArgsConstructor
public class PenaltyController {
    private final PenaltyService penaltyService;

    @PostMapping
    public ResponseEntity<Response<PenaltyDto>> createPenalty(
            @Valid @RequestBody PenaltyDto penaltyDto) {

        Response<PenaltyDto> income = penaltyService.createIncome(penaltyDto);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(income.getCode()))
                .body(income);

    }

    @PatchMapping
    public ResponseEntity<Response<PenaltyDto>> updatePenalty(
            @RequestBody PenaltyDto penaltyDto) {

        Response<PenaltyDto> income = penaltyService.updateIncome(penaltyDto);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(income.getCode()))
                .body(income);

    }

    @GetMapping("/{penaltyId}")
    public ResponseEntity<Response<PenaltyDto>> getPenalty(
            @PathVariable("penaltyId") UUID incomeId) {

        Response<PenaltyDto> income = penaltyService.getIncomeById(incomeId);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(income.getCode()))
                .body(income);

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Financial Secretary, Treasurer, President')")
    public ResponseEntity<Response<PaginatedResponse<PenaltyDto>>> getAllPenalty(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Response<PaginatedResponse<PenaltyDto>> income = penaltyService.incomes(page, size);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(income.getCode()))
                .body(income);
    }

    @GetMapping("/all-expenses")
    public ResponseEntity<Response<PaginatedResponse<PenaltyDto>>> getAllMyExpenses(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Response<PaginatedResponse<PenaltyDto>> income = penaltyService.getAllExpenses(page, size);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(income.getCode()))
                .body(income);

    }
}
