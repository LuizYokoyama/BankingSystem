package io.github.LuizYokoyama.SchedularPayments.controller;

import io.github.LuizYokoyama.SchedularPayments.dto.CreateRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.RecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.service.SchedularService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchedularPaymentsController {

    @Autowired
    SchedularService schedularService;

    @Operation(summary = "Post order to CREATE a scheduled payment.")
    @ApiResponses({ @ApiResponse(responseCode = "201", description = "The order has been posted and the scheduled payment created.")})
    @PostMapping("create_scheduled_payment")
    public ResponseEntity<RecurrenceDto> createScheduledPayment(@RequestBody CreateRecurrenceDto createRecurrenceDto) {

        return schedularService.schedule(createRecurrenceDto);

    }

    @Operation(summary = "Put order to EDIT the scheduled payment.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The scheduled payment edited OK.")})
    @PutMapping("edit_scheduled_payment")
    public String editScheduledPayment() {
        //TODO
        return "Recurrence payment test EDITED OK";

    }

    @Operation(summary = "Put order to CANCEL the scheduled payment.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The scheduled payment was canceled.")})
    @PutMapping("cancel_scheduled_payment")
    public String cancelScheduledPayments() {
        //TODO
        return "Recurrence payment test CANCELED OK";

    }
}
