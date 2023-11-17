package io.github.LuizYokoyama.BankAccount.controller;

import io.github.LuizYokoyama.BankAccount.dto.*;
import io.github.LuizYokoyama.BankAccount.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1")
public class BankAccountController {

    @Autowired
    AccountService accountService;

    @Operation(summary = "CREATE a new bank account.")
    @ApiResponses({ @ApiResponse(responseCode = "201", description = "The account was created OK.")})
    @PostMapping("accounts")
    public ResponseEntity<AccountCreatedDto> createAccount(@RequestBody CreateAccountDto createAccountDto) {

        return accountService.createAccount(createAccountDto);

    }

    @Operation(summary = "deposit the amount in the account.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The deposit was executed OK.")})
    @PutMapping("accounts/{id}")
    public ResponseEntity<EntryDto> deposit(@PathVariable(value = "id") Integer id, @RequestBody DepositDto depositDto) {

        return accountService.deposit(id, depositDto);

    }

    @Operation(summary = "Bank account statement.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The bank statement was found and returned.")})
    @PutMapping("statements/{id}")
    public ResponseEntity<BankStatementDto> statement(@PathVariable(value = "id") Integer id, @RequestBody PeriodDto periodDto) {

        return accountService.statement(id, periodDto);

    }

}
