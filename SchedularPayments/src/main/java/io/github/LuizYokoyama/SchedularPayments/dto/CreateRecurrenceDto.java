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
public class CreateRecurrenceDto implements IRecurrenceDto{

    @Positive
    private int accountId;

    @NotNull
    private String recurrenceName;

    @NotNull
    private LocalDate occurrenceDate;

    @Positive
    @Max(value = MAX_DURATION, message = "Forneça no máximo a duração de " + MAX_DURATION + " recorrências!")
    @Min(value = MIN_DURATION, message = "Forneça no mínimo a duração de " + MIN_DURATION + " recorrência!")
    private int duration;

    @Positive
    private float value;

    @Positive
    private int accountDestinationID;

}
