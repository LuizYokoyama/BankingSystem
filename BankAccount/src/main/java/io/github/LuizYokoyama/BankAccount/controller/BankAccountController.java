package io.github.LuizYokoyama.BankAccount.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountController {

    @Operation(summary = "Post order to CREATE a new bank account.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The order has been posted and the account was created OK.")})
    @PostMapping("create_scheduled_payment")
    public String createAccount() {
        return "Account test CREATED OK";

    }

    @Operation(summary = "Post order to deposit the amount in the account.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The deposit was executed OK.")})
    @PostMapping("deposit")
    public String deposit() {
        return "Deposit test OK";

    }

    @Operation(summary = "Get the Bank statement.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The Bank statement was found.")})
    @GetMapping("statement")
    public String statement() {
        return "Bank statement test OK";

    }

}
