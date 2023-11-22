package io.github.LuizYokoyama.SchedularPayments.controller;

import io.github.LuizYokoyama.SchedularPayments.dto.CreateRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.EditRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.RecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.service.SchedularService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1")
public class SchedularPaymentsController {

    @Autowired
    SchedularService schedularService;

    @Operation(summary = "CREATE a scheduled payment.")
    @ApiResponses({ @ApiResponse(responseCode = "201", description = "The scheduled payment was created.")})
    @PostMapping("recurrences")
    public ResponseEntity<RecurrenceDto> createScheduledPayment(@RequestBody CreateRecurrenceDto createRecurrenceDto) {

        RecurrenceDto recurrenceDto = schedularService.schedule(createRecurrenceDto);
        if (recurrenceDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(recurrenceDto);
    }

    @Operation(summary = "EDIT the scheduled payment.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The scheduled payment edited OK.")})
    @PatchMapping("recurrences/{id}")
    public ResponseEntity<RecurrenceDto> editScheduledPayment(@PathVariable(value = "id") UUID uuid, @RequestBody EditRecurrenceDto editRecurrenceDto) {

        RecurrenceDto recurrenceDto = schedularService.editScheduled(uuid, editRecurrenceDto);
        if (recurrenceDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(recurrenceDto);
    }

    @Operation(summary = "CANCEL the scheduled payment.")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "The scheduled payment was canceled.")})
    @DeleteMapping("recurrences/{id}")
    public ResponseEntity<RecurrenceDto> cancelScheduledPayments(@PathVariable(value = "id") UUID uuid) {

        RecurrenceDto recurrenceDto = schedularService.cancelScheduledPayment(uuid);
        if (recurrenceDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(recurrenceDto);

    }
}
