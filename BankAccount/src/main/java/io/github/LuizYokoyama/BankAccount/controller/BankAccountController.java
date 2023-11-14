package io.github.LuizYokoyama.BankAccount.controller;

import io.github.LuizYokoyama.BankAccount.dto.AccountCreatedDto;
import io.github.LuizYokoyama.BankAccount.dto.AccountDto;
import io.github.LuizYokoyama.BankAccount.dto.CreateAccountDto;
import io.github.LuizYokoyama.BankAccount.dto.DepositDto;
import io.github.LuizYokoyama.BankAccount.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BankAccountController {

    @Autowired
    AccountService accountService;

    @Operation(summary = "Post order to CREATE a new bank account.")
    @ApiResponses({ @ApiResponse(responseCode = "201", description = "The order has been posted and the account was created OK.")})
    @PostMapping("create_bank_account")
    public ResponseEntity<AccountCreatedDto> createAccount(@RequestBody CreateAccountDto createAccountDto) {

        return accountService.createAccount(createAccountDto);

    }

    @Operation(summary = "Put order to deposit the amount in the account.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The deposit was executed OK.")})
    @PutMapping("deposit/{account_id}")
    public ResponseEntity<AccountCreatedDto> deposit(@PathVariable(value = "account_id") Integer id, @RequestBody DepositDto depositDto) {

        return accountService.deposit(id, depositDto);

    }

    @Operation(summary = "Get the Bank statement.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The Bank statement was found.")})
    @GetMapping("statement")
    public String statement() {
        //TODO
        return "Bank statement test OK";

    }

}
