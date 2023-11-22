package io.github.LuizYokoyama.SchedularPayments.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateRecurrenceDto extends RecurrenceDto{

    @Positive
    private int accountId;

    @NotNull
    private String recurrenceName;

    @NotNull
    private LocalDate occurrenceDate;

    @Positive
    @Max(12)
    @Min(1)
    private int duration;

    @Positive
    private float value;

    @Positive
    private int accountDestinationID;

}
