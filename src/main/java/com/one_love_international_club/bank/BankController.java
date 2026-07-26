package com.one_love_international_club.bank;

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
@RequestMapping("/v1/banks")
@Tag(name = "Bank Controller", description = "Controller class for all banks related endpoints.")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @PostMapping
    @PreAuthorize("hasAnyRole('Treasurer', 'Financial Secretary', 'President')")
    public ResponseEntity<Response<BankDto>> createBank(
            @Valid @RequestBody BankDto bankDto
    ) {
        Response<BankDto> bank = bankService.createBank(bankDto);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(bank.getCode()))
                .body(bank);
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('Treasurer', 'Financial Secretary', 'President')")
    public ResponseEntity<Response<BankDto>> updateBank(
            @RequestBody BankDto bankDto
    ) {
        Response<BankDto> bank = bankService.updateBank(bankDto);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(bank.getCode()))
                .body(bank);
    }

    @GetMapping("/{bankId}")
    public ResponseEntity<Response<BankDto>> getBank(
            @PathVariable("bankId") UUID bankId
    ) {
        Response<BankDto> bank = bankService.getBank(bankId);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(bank.getCode()))
                .body(bank);
    }

    @GetMapping
    public ResponseEntity<Response<List<BankDto>>> getBanks() {
        Response<List<BankDto>> bank = bankService.getBanks();

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(bank.getCode()))
                .body(bank);
    }

    @DeleteMapping("/{bankId}")
    @PreAuthorize("hasAnyRole('Treasurer', 'Financial Secretary', 'President')")
    public ResponseEntity<Response<Void>> deleteBank(
            @PathVariable("bankId") UUID bankId
    ) {
        Response<Void> bank = bankService.removeBank(bankId);

        return ResponseEntity.status(StatusCodeResolver.getHttpStatus(bank.getCode()))
                .body(bank);
    }
}
