package io.github.LuizYokoyama.SchedularPayments.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchedularPaymentsController {

    @Operation(summary = "Post order to CREATE a recurrence payment.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The order has been posted and the recurring payment created.")})
    @PostMapping("create_scheduled_payment")
    public String createScheduledPayment() {
        return "Recurrence payment test CREATED OK";

    }

    @Operation(summary = "Put order to EDIT the recurrence payment.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The recurring payment edited OK.")})
    @PutMapping("edit_scheduled_payment")
    public String editScheduledPayment() {
        return "Recurrence payment test EDITED OK";

    }

    @Operation(summary = "Put order to CANCEL the scheduled payment.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The scheduled payment was canceled.")})
    @PutMapping("cancel_scheduled_payment")
    public String cancelScheduledPayments() {
        return "Recurrence payment test CANCELED OK";

    }
}
