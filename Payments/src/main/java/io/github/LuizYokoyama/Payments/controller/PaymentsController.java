package io.github.LuizYokoyama.Payments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentsController {

    @Operation(summary = "list of payments scheduled for today.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The list was found and returned.")})
    @GetMapping("payments")
    public String getPayments() {
        //TODO
        return "Payments test list ...";

    }

    @Operation(summary = "make scheduled payments for today.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "executed.")})
    @PutMapping("payments")
    public String executeScheduledPayments() {
        //TODO
        return "Payments test executed ok";

    }


}
