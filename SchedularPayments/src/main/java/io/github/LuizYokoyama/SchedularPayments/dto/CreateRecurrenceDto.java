package io.github.LuizYokoyama.SchedularPayments.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateRecurrenceDto {

    @Positive
    private int accountId;

    @NotNull
    private String recurrenceName;

    @NotNull
    private LocalDate occurrenceDate;

    @Positive
    private int duration;

    @Positive
    private float value;

    @Positive
    private int accountDestinationID;

}
