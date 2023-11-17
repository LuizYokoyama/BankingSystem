package io.github.LuizYokoyama.SchedularPayments.controller;

import io.github.LuizYokoyama.SchedularPayments.dto.CreateRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.EditRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.RecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.service.SchedularService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1")
public class SchedularPaymentsController {

    @Autowired
    SchedularService schedularService;

    @Operation(summary = "CREATE a scheduled payment.")
    @ApiResponses({ @ApiResponse(responseCode = "201", description = "The scheduled payment created.")})
    @PostMapping("recurrences")
    public ResponseEntity<RecurrenceDto> createScheduledPayment(@RequestBody CreateRecurrenceDto createRecurrenceDto) {

        return schedularService.schedule(createRecurrenceDto);

    }

    @Operation(summary = "EDIT the scheduled payment.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The scheduled payment edited OK.")})
    @PatchMapping("recurrences/{id}")
    public ResponseEntity<RecurrenceDto> editScheduledPayment(@PathVariable(value = "id") UUID uuid, @RequestBody EditRecurrenceDto editRecurrenceDto) {

        //return schedularService.editScheduled(uuid, editRecurrenceDto);
        //TODO
        return null;
    }

    @Operation(summary = "CANCEL the scheduled payment.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The scheduled payment was canceled.")})
    @DeleteMapping("recurrences/{id}")
    public String cancelScheduledPayments(@PathVariable(value = "id") UUID uuid) {
        //TODO
        return "Recurrence payment test CANCELED OK";

    }
}
