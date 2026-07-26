package com.one_love_international_club.income;

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
@RequestMapping("/v1/incomes")
@Tag(name = "Income Controller", description = "Controller class for all income related endpoints.")
@RequiredArgsConstructor
public class IncomeController {
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<Response<IncomeDto>> createIncome(
            @Valid @RequestBody IncomeDto incomeDto) {

        Response<IncomeDto> income = incomeService.createIncome(incomeDto);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(income.getCode()))
                .body(income);

    }

    @PatchMapping
    public ResponseEntity<Response<IncomeDto>> updateIncome(
            @RequestBody IncomeDto incomeDto) {

        Response<IncomeDto> income = incomeService.updateIncome(incomeDto);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(income.getCode()))
                .body(income);

    }


    @GetMapping("/{incomeId}")
    public ResponseEntity<Response<IncomeDto>> getIncome(
            @PathVariable("incomeId") UUID incomeId) {

        Response<IncomeDto> income = incomeService.getIncomeById(incomeId);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(income.getCode()))
                .body(income);

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Financial Secretary, Treasurer, President')")
    public ResponseEntity<Response<PaginatedResponse<IncomeDto>>> getAllIncome(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Response<PaginatedResponse<IncomeDto>> income = incomeService.incomes(page, size);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(income.getCode()))
                .body(income);

    }

    @GetMapping("/all-expenses")
    public ResponseEntity<Response<PaginatedResponse<IncomeDto>>> getAllMyExpenses(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Response<PaginatedResponse<IncomeDto>> income = incomeService.getAllExpenses(page, size);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(income.getCode()))
                .body(income);

    }
}
