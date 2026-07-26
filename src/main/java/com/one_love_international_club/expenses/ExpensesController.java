package com.one_love_international_club.expenses;

import com.one_love_international_club.setting.dto.Response;
import com.one_love_international_club.setting.dto.response.PaginatedResponse;
import com.one_love_international_club.util.StatusCodeResolver;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/expenses")
@Tag(name = "Expense Controller", description = "Controller class for all expenses related endpoints.")
@RequiredArgsConstructor
public class ExpensesController {
    private final ExpensesService expensesService;

    @PostMapping
    @PreAuthorize("hasAnyRole('Financial Secretary, Treasurer, President')")
    public ResponseEntity<Response<ExpensesDto>> createExpense(
            @Valid @RequestBody ExpensesDto expensesDto
    ) {
        Response<ExpensesDto> expenses = expensesService.createExpenses(expensesDto);

        return ResponseEntity.
                status(StatusCodeResolver.getHttpStatus(expenses.getCode()))
                .body(expenses);
    }


    @PatchMapping
    @PreAuthorize("hasAnyRole('Financial Secretary, Treasurer, President')")
    public ResponseEntity<Response<ExpensesDto>> updateExpense(
            @RequestBody ExpensesDto expensesDto
    ) {
        Response<ExpensesDto> expenses = expensesService.updateExpense(expensesDto);

        return ResponseEntity.
                status(StatusCodeResolver.getHttpStatus(expenses.getCode()))
                .body(expenses);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Financial Secretary, Treasurer, President')")
    public ResponseEntity<Response<PaginatedResponse<ExpensesDto>>> Expenses(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        Response<PaginatedResponse<ExpensesDto>> expenses = expensesService.getAllExpenses(page, size);

        return ResponseEntity.
                status(StatusCodeResolver.getHttpStatus(expenses.getCode()))
                .body(expenses);
    }

    @GetMapping("/my-benefits")
    public ResponseEntity<Response<PaginatedResponse<ExpensesDto>>> myBenefits(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        Response<PaginatedResponse<ExpensesDto>> expenses = expensesService.getAllMyBenefits(page, size);

        return ResponseEntity.
                status(StatusCodeResolver.getHttpStatus(expenses.getCode()))
                .body(expenses);
    }


}
