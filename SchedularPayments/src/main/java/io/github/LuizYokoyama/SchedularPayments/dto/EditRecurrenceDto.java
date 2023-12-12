package io.github.LuizYokoyama.SchedularPayments.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EditRecurrenceDto implements IRecurrenceDto{

    @NotNull
    private LocalDate occurrenceDate;

    @Positive
    @Max(value = MAX_DURATION, message = "Forneça no máximo a duração de " + MAX_DURATION + " recorrências!")
    @Min(value = MIN_DURATION, message = "Forneça no mínimo a duração de " + MIN_DURATION + " recorrência!")
    private int monthsDuration;

    @Positive
    private BigDecimal value;

}
