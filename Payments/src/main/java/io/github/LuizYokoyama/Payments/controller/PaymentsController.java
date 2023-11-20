package io.github.LuizYokoyama.Payments.controller;

import io.github.LuizYokoyama.Payments.service.PaymentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;


    @Operation(summary = "make scheduled payments for today.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Returns the number of scheduled payments executeds.")})
    @PutMapping("payments")
    public int executeScheduledPayments() {

        return paymentsService.executePays();

    }


}
